package com.cwh.service.impl;

import com.cwh.bo.SkuBo;
import com.cwh.bo.SpuBo;
import com.cwh.common.utils.JsonUtils;
import com.cwh.common.utils.PageResult;
import com.cwh.entity.Sku;
import com.cwh.entity.Spu;
import com.cwh.entity.SpuDetail;
import com.cwh.entity.Stock;
import com.cwh.mapper.SkuMapper;
import com.cwh.mapper.SpuDetailMapper;
import com.cwh.mapper.SpuMapper;
import com.cwh.mapper.StockMapper;
import com.cwh.service.GoodsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-23 19:42
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private SpuMapper spuMapper;
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private StockMapper stockMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<Spu> list(String sortBy, Boolean desc, Integer page, Integer pageNum, String search, Integer saleable) {
        PageHelper.startPage(page, pageNum);
        String sort = (desc?"asc":"desc");
        if (saleable == 2){
            saleable = null;
        }
        Page<Spu> page1  = (Page<Spu>) spuMapper.selectConditionAll(sortBy, sort, page, pageNum,search,saleable);
        return new PageResult<Spu>(page1.getTotal(),page1.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public Integer saveGoods(HashMap<String ,Object> map) throws InstantiationException, IllegalAccessException {
        //spu
        Spu spu = autoBeanMap(map, Spu.class);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spuMapper.insertSelective(spu);
        //supDetail
        Map spuDetail = (Map) map.get("spuDetail");
        SpuDetail spuDetail1 = autoBeanMap(spuDetail, SpuDetail.class);
        spuDetail1.setSpuId(spu.getId());
        spuDetailMapper.insertSelective(spuDetail1);
        //skus
        String serialize = JsonUtils.serialize(map.get("skus"));
        List<SkuBo> skuBos = JsonUtils.parseList(serialize, SkuBo.class);
        Sku sku = null;
        Stock stock = null;
        //sku
        List<Sku> skuList = new ArrayList<>();
        // 库存
        List<Stock> stockList = new ArrayList<>();
        for (SkuBo skuBo : skuBos) {
            //sku
            sku  = new Sku();
            BeanUtils.copyProperties(skuBo,sku);
            if (sku != null){
                sku.setSpuId(spu.getId());
                sku.setEnable(true);
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(new Date());
                skuMapper.insertSelective(sku);
            }
            skuList.add(sku);
            //库存
            stock = new Stock();
            BeanUtils.copyProperties(skuBo,stock);
            if (stock != null){
                stock.setSkuId(sku.getId());
                stockMapper.insertSelective(stock);
            }
            stockList.add(stock);
        }

        return null;
    }

    public <T> T autoBeanMap (Map map,Class<T> tClass) throws IllegalAccessException, InstantiationException {
        if (map.isEmpty()){
            return null;
        }
        T instance = tClass.newInstance();
        //获取类的所有成员字段包括私有
        Field[] fields = tClass.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            //使安全访问失效
            field.setAccessible(true);
            //获取属性名
            String name = field.getName();
            //查看map中时候存在相同的键
            if (map.containsKey(name)){
                if (null !=map.get(name)){
                    try {
                        if (field.getType().getName().contains("Long")){
                            Object o = map.get(name);
                            field.set(instance,Long.valueOf(String.valueOf(o)).longValue());
                        }else {
                            //属性赋值
                            field.set(instance,map.get(name).toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return instance;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoods(Long id) {
        spuMapper.deleteByPrimaryKey(id);
        spuDetailMapper.deleteByPrimaryKey(id);
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> select = skuMapper.select(sku);
        stockMapper.deleteByIdList(select.stream().map(Sku::getId).collect(Collectors.toList()));
        skuMapper.delete(sku);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editGoodsSaleable(Long id, Boolean saleable) {
        Spu spu = new Spu();
        spu.setSaleable(saleable);
        spu.setId(id);
        spuMapper.updateByPrimaryKeySelective(spu);
        skuFalse(id);
    }

    public void skuFalse(Long id){
        //设置sku 失效
        Sku sku = new Sku();
        sku.setEnable(false);
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        skuMapper.updateByExampleSelective(sku,example);
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long pid) {
        return spuDetailMapper.selectByPrimaryKey(pid);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long pid) {
        Sku sku = new Sku();
        sku.setSpuId(pid);
        List<Sku> skuList = skuMapper.select(sku);
        for (Sku sku1 : skuList) {
            sku1.setStock(stockMapper.selectByPrimaryKey(sku1.getId()).getStock());
        }
        return skuList;
    }


    /**
    *@Description: 修改商品
    *@Param: [spuBo]
    *@return: void
    *@Author: cuiweihua
    *@date: 2020/7/30
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editGoods(SpuBo spuBo) {
        Date date = new Date();
        //修改spu
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo,spu);
        spu.setLastUpdateTime(date);
        spu.setSaleable(null);
        spu.setValid(null);
        spuMapper.updateByPrimaryKeySelective(spu);
        //修改spudetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        //删除sku和库存表数据
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        List<Sku> select = skuMapper.select(sku);
        //删除库存
        List<Long> collect = select.stream().map(Sku::getId).collect(Collectors.toList());
        stockMapper.deleteByIdList(collect);
        //删除出sku
        skuMapper.delete(sku);
        //保存sku 库存
        List<SkuBo> skus = spuBo.getSkus();
        Stock stock = null;

        for (SkuBo skuBo : skus) {
            BeanUtils.copyProperties(skuBo,sku);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(date);
            sku.setEnable(true);
            sku.setLastUpdateTime(date);
            skuMapper.insertSelective(sku);
            stock = new Stock();
            stock.setStock(skuBo.getStock());
            stock.setSkuId(sku.getId());
            stockMapper.insertSelective(stock);
        }
    }
}
