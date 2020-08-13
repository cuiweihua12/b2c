package com.cwh.mapper;


import com.cwh.entity.SpecParam;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;

@Mapper
public interface SpecParamMapper extends tk.mybatis.mapper.common.Mapper<SpecParam>, SelectByIdListMapper<SpecParam,Long> {


}
