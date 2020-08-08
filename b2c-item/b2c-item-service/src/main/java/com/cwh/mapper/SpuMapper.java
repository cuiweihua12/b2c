package com.cwh.mapper;

import com.cwh.entity.Spu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Spu record);

    int insertSelective(Spu record);

    Spu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Spu record);

    int updateByPrimaryKey(Spu record);

    List<Spu> selectConditionAll(@Param("sortBy") String sortBy,
                                 @Param("sort") String sort,
                                 @Param("page") Integer page,
                                 @Param("pageNum") Integer pageNum,
                                 @Param("search") String search,
                                 @Param("saleable") Integer saleable);
}
