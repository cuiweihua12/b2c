package com.cwh.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品规格参数模板，json格式。(TbSpecification)实体类
 *
 * @author cuiweihua
 * @since 2020-07-23 13:51:59
 */
@ApiModel(description = "商品规格参数模板，json格式。(TbSpecification)实体类")
@Data
public class Specification implements Serializable {
    private static final long serialVersionUID = -17964569210733080L;
    /**
     * 规格模板所属商品分类id
     */
    private Long categoryId;
    /**
     * 规格参数模板，json格式
     */
    private String specifications;

}
