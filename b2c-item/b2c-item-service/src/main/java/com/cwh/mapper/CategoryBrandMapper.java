package com.cwh.mapper;

import com.cwh.entity.CategoryBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryBrandMapper {
    int deleteByPrimaryKey(CategoryBrand key);

    int insert(CategoryBrand record);

    int insertSelective(CategoryBrand record);

    void deleteByBrand(Long id);

    CategoryBrand selectCategoryByBrand(@Param("id") Long id);
}
