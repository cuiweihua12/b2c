package com.cwh.service.impl;

import com.cwh.common.result.CommonResult;
import com.cwh.common.utils.PageResult;
import com.cwh.entity.Brand;
import com.cwh.entity.Category;
import com.cwh.entity.CategoryBrand;
import com.cwh.mapper.BrandMapper;
import com.cwh.mapper.CategoryBrandMapper;
import com.cwh.mapper.CategoryMapper;
import com.cwh.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-16 12:08
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryBrandMapper categoryBrandMapper;
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Brand saveBrand(Brand brand){
        if (brand != null){
            CategoryBrand categoryBrand = null;
            if (brand.getId() != null){
                brandMapper.updateByPrimaryKeySelective(brand);
                //删除关系表中数据
                categoryBrandMapper.deleteByBrand(brand.getId());
                //将新关系保存至关系表
                saveCateBran(categoryBrand,brand);
            }else{
                //保存品牌
                brandMapper.insertSelective(brand);
                //保存品牌类别关系
                saveCateBran(categoryBrand,brand);
            }
        }
        return brand;
    }

    private void saveCateBran(CategoryBrand categoryBrand,Brand brand){
        for (Long category : brand.getCategories()) {
            categoryBrand = new CategoryBrand();
            categoryBrand.setBrandId(brand.getId());
            categoryBrand.setCategoryId(category);
            categoryBrandMapper.insert(categoryBrand);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public CommonResult deleteBrand(Long id) {
        try {
            //删除关系表中数据
            categoryBrandMapper.deleteByBrand(id);
            //删除商品
            brandMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.RESULT_ERROR;
        }
        return CommonResult.RESULT_SUCCESS;
    }

    /**
    *@Description: 保存品牌，并且在保存品牌的同时保存品牌和类别关系
    *@Param: [brand]
    *@return: java.util.List<com.cwh.entity.Brand>
    *@Author: cuiweihua
    *@date: 2020/7/16
    */
    @Override
    @Transactional(readOnly = true)
    public PageResult<Brand> list(Brand brand, Integer page, Integer pageNum, String sortBy, Boolean sortDesc) {
        //使用分页插件
        PageHelper.startPage(page,pageNum);
        String sort = (sortDesc?"asc":"desc");
        Page<Brand> brands = (Page<Brand>) brandMapper.selectAll(sort,sortBy,brand.getName());
        return new PageResult<Brand>(brands.getTotal(),brands.getResult());

    }

    @Override
    /**
    *@Description: 根基条件查询
    *@Param: [brand]
    *@return: com.cwh.entity.Brand
    *@Author: cuiweihua
    *@date: 2020/7/17
    */
    public List<Brand> criteraQuery(Brand brand) {

        return  brandMapper.criteraQuery(brand);
    }

    @Override
    public Map<String, Object> queryById(Brand brand) {
        CategoryBrand categoryBrand = null;
        //查询品牌信息
        Brand byPrimaryKey = brandMapper.selectByPrimaryKey(brand.getId());
        //查询品牌分类信息
        List<Category> categoryList = categoryMapper.selectBrandCategoryAll(brand.getId());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("brand",byPrimaryKey);
        hashMap.put("category",categoryList);
        return  hashMap;
    }

    @Override
    public List<Brand> searchBrandAll() {
        return brandMapper.selectAll(null,null,null);
    }

    @Override
    public List<Brand> queryBrandByCategoryId(Long cateGoryId) {
        return brandMapper.selectBrandByCategoryId(cateGoryId);
    }

    @Override
    public Brand queryByBrandId(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }

    @Override
    public List<Brand> queryByIdList(List<Long> ids) {

        return brandMapper.queryByIdList(ids);
    }
}
