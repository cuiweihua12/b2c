package com.cwh.mapper;

import com.cwh.entity.Stock;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;

@Mapper
public interface StockMapper extends tk.mybatis.mapper.common.Mapper<Stock>, DeleteByIdListMapper<Stock,Long> {
}
