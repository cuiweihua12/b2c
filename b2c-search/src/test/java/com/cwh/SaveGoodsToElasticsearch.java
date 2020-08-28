package com.cwh;

import com.cwh.client.ConnectionBrandClient;
import com.cwh.client.ConnectionCategoryClient;
import com.cwh.client.ConnectionGoodsClient;
import com.cwh.client.ConnectionSpecClient;
import com.cwh.common.utils.JsonUtils;
import com.cwh.dao.GoodsCWHRepository;
import com.cwh.entity.*;
import com.cwh.pojo.GoodsCWH;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: b2c
 * @description: 将mysql 库中数据转如es 试用SaveGoodsToElasticsearchTest测试类，此类作废
 * @author: cuiweihua
 * @create: 2020-08-13 10:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveGoodsToElasticsearch {

    @Autowired
    private GoodsCWHRepository goodsCWHRepository;

    @Autowired
    private ConnectionGoodsClient goodsClient;

    @Autowired
    private ConnectionBrandClient brandClient;

    @Autowired
    private ConnectionCategoryClient categoryClient;

    @Autowired
    private ConnectionSpecClient specClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void getGoods(){
        GoodsCWH goods = null;
        //商品集合
        List<GoodsCWH> goodsList = new ArrayList<>();
        //价格集合
        List<Long> priceList = null;

        //sku集合
        List<Map> skuList = null;
        //储存需要的skus
        HashMap<String, Object> hashMap = null;
        StringBuffer buffer = null;
        List<Spu> items = goodsClient
                .list(null, true, 0, 10000, "", 2)
                .getBody()
                .getItems();
        for (Spu item : items) {

            buffer = new StringBuffer();

            //品牌
            String brand = brandClient.queryByBrandId(item.getBrandId()).getBody().getName();
            buffer.append(brandClient.queryByBrandId(item.getBrandId()).getBody().getName());
            //标题
            buffer.append(item.getTitle());
            //分类
            String category = categoryClient.queryCategory(
                    Arrays.asList(item.getCid1(), item.getCid2(), item.getCid3())
            ).getBody()
                    .stream()
                    .map(Category::getName)
                    .collect(Collectors.joining());
            buffer.append(
                    categoryClient.queryCategory(
                            Arrays.asList(item.getCid1(),item.getCid2(),item.getCid3())
                    ).getBody()
                            .stream()
                            .map(Category::getName)
                            .collect(Collectors.joining())
            );

            //skus 将获取到的数据进行抽取，只保存自己需要的
            List<Sku> skusList = goodsClient.querySkusBySpuId(item.getId()).getBody();

            skuList = new ArrayList<>();
            for (Sku skus : skusList) {
                priceList = new ArrayList<>();
                hashMap = new HashMap<>();
                hashMap.put("id",skus.getId());
                hashMap.put("price",skus.getPrice());
                hashMap.put("image", StringUtils.isEmpty(skus.getImages())?"":skus.getImages());
                hashMap.put("title",skus.getTitle());
                skuList.add(hashMap);
                //价格集合
                priceList.add(skus.getPrice());
            }
            //规格参数值
            SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(item.getId()).getBody();
            //通用属性
            Map<Long, String> genericeMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
            //特殊属性
            //Map<Long, List> specialMap = JsonUtils.parseMap(spuDetail.getSpecialSpec(), Long.class, List.class);
            Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>(){});

            //商品规格
            Map<String, Object>  map = new HashMap<>();
            //  规格参数
            specClient.searchSpecParamsBySpecGroupId(null, item.getCid3(),null)
                    .getBody()
                    //将参数与参数值做数据整合
                    .forEach(specParam -> {
                        if (specParam.getSearching()){
                            if (specParam.getGeneric()){
                                if (specParam.getNumeric()){
                                    map.put(specParam.getName(),chooseSegment(genericeMap.get(specParam.getId()),specParam));
                                }else {
                                    map.put(specParam.getName(),genericeMap.get(specParam.getId()));
                                }
                            }else {
                                map.put(specParam.getName(),specialMap.get(specParam.getId()));
                            }
                        }
                    });
            goods = new GoodsCWH(
                    item.getId(),
                    item.getTitle(),
                    brand,
                    category,
                    item.getSubTitle(),
                    item.getBrandId(),
                    item.getCid1(),
                    item.getCid2(),
                    item.getCid3(),
                    item.getCreateTime(),
                    priceList,
                    JsonUtils.serialize(skuList),
                    map
            );
            goodsList.add(goods);
        }
        goodsList.forEach(goodsCWH -> {
            goodsCWH.getSpecs( ).forEach((k,v)->{
                System.out.println(k+"           "+v);
            });
        });
        goodsCWHRepository.saveAll(goodsList);
    }
    /**
     * 将规格参数分段
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
