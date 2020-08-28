package com.cwh.service;

import com.cwh.entity.Category;

import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-15 19:05
 */
public interface CategoryService {
    List<Category> queryCategoryByPid(Long pid);

    String removeCategoryById(Long id);

    void editCategoryById(Category category);

    Category saveCategory(Category category);

    Map<String,Object> getCategoryById(Long id);

    List<Category> queryCategory(List<Long> ids);

    List<Category> queryCategoryAll();
}
