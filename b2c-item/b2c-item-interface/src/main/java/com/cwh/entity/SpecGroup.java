package com.cwh.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 规格参数的分组表，每个商品分类下有多个规格参数组(TbSpecGroup)实体类
 *
 * @author makejava
 * @since 2020-07-24 16:55:52
 */
@Data
@ApiModel(description = "商品分类组信息")
@Table(name = "tb_spec_group")
public class SpecGroup implements Serializable {
    private static final long serialVersionUID = -71580655583103187L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 商品分类id，一个分类下有多个规格组
     */
    @ApiModelProperty(value = "商品分类id，一个分类下有多个规格组")
    private Long cid;
    /**
     * 规格组的名称
     */
    @ApiModelProperty(value = "规格组的名称")
    @Column(name = "`name`")
    private String name;


    @Transient
    private List<SpecParam> params;


}
