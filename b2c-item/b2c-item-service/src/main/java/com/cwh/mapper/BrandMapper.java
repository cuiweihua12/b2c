package com.cwh.mapper;


import com.cwh.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    int insertSelective(Brand record);

    Brand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Brand record);

    int updateByPrimaryKey(Brand record);

    List<Brand> criteraQuery(Brand brand);

    List<Brand> selectAll(@Param("sort") String sort,@Param("sortBy") String sortBy, @Param("name") String name);

    List<Brand> selectBrandByCategoryId(@Param("cateGoryId") Long cateGoryId);
}
