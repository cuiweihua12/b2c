package com.cwh.mapper;

import com.cwh.entity.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-06 20:54
 */
@org.apache.ibatis.annotations.Mapper
public interface CategoryMapperTK  extends Mapper<Category> , SelectByIdListMapper<Category,Long> {
}
