package com.cwh.mapper;

import com.cwh.entity.Specification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpecificationMapper {
    int deleteByPrimaryKey(Long categoryId);

    int insert(Specification record);

    int insertSelective(Specification record);

    Specification selectByPrimaryKey(Long categoryId);

    int updateByPrimaryKeySelective(Specification record);

    int updateByPrimaryKey(Specification record);

}
