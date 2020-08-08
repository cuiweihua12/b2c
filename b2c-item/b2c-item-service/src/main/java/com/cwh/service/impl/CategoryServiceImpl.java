package com.cwh.service.impl;

import com.cwh.entity.Brand;
import com.cwh.entity.Category;
import com.cwh.mapper.BrandMapper;
import com.cwh.mapper.CategoryMapperTK;
import com.cwh.service.CategoryService;
import com.cwh.mapper.CategoryMapper;
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
 * @create: 2020-07-15 19:05
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryMapperTK categoryMapperTK;

    @Override
    public List<Category> queryCategoryByPid(Long parentId) {
        return categoryMapper.selectByPid(parentId);
    }

    /**
    *@Description: 删除商品分类，删除后无子节点后将是否为父节点状态修改
    *@Param: [id]
    *@return: java.lang.Integer
    *@Author: cuiweihua
    *@date: 2020/7/17
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public String removeCategoryById(Long id) {
        try {
            //获取父节点下共几个节点
            Category category = categoryMapper.selectByPrimaryKey(id);
            List<Category> categoryList = categoryMapper.selectByPid(category.getParentId());
            if (categoryList.size() == 1){
                //如只有一个节点将父节点状态设置为false
                Category pCategory = new Category();
                pCategory.setId(category.getParentId());
                pCategory.setIsParent(false);
                categoryMapper.updateByPrimaryKeySelective(pCategory);

            }
            //删除
            categoryMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }

        return "删除成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void editCategoryById(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        Category newCate = new Category();
        categoryMapper.insertSelective(category);
        //修改父级状态
        newCate.setId(category.getParentId());
        newCate.setIsParent(true);
        categoryMapper.updateByPrimaryKeySelective(newCate);

        return category;
    }

    @Override
    /**
    *@Description: 通过品牌id查询商品信息
    *@Param: [id]
    *@return: com.cwh.entity.Category
    *@Author: cuiweihua
    *@date: 2020/7/16
    */
    @Transactional(readOnly = true)
    public Map<String,Object> getCategoryById(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        Category category = new Category();
        category.setParentId(id);
        //查询类别信息
        Category categoryByBrandId = categoryMapper.getCategoryByBrandId(id);
        //查询品牌信息
        Brand brand = brandMapper.selectByPrimaryKey(id);
        map.put("category",category);
        map.put("brand",brand);
        return map;
    }

    @Override
    public List<Category> queryCategory(List<Long> ids) {

        return categoryMapperTK.selectByIdList(ids);
    }
}
