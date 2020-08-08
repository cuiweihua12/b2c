package com.cwh.mapper;

import com.cwh.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectByPid(Long parentId);

    Category getCategoryByBrandId(@Param("bid") Long bid);

    List<Category> selectBrandCategoryAll(Long id);
}
