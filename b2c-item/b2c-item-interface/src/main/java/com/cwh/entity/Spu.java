package com.cwh.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * tb_spu
 * @author
 */
@Data
@ApiModel(description = "商品列表信息实体类")
public class Spu implements Serializable {
    /**
     * spu id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 子标题
     */
    @ApiModelProperty(value = "子标题")
    private String subTitle;

    /**
     * 1级类目id
     */
    @ApiModelProperty(value = "1级类目id")
    private Long cid1;

    /**
     * 2级类目id
     */
    @ApiModelProperty(value = "2级类目id")
    private Long cid2;

    /**
     * 3级类目id
     */
    @ApiModelProperty(value = "3级类目id")
    private Long cid3;

    /**
     * 商品所属品牌id
     */
    @ApiModelProperty(value = "商品所属品牌id")
    private Long brandId;

    /**
     * 是否上架，0下架，1上架
     */
    @ApiModelProperty(value = "是否上架，0下架，1上架")
    private Boolean saleable;

    /**
     * 是否有效，0已删除，1有效
     */
    @ApiModelProperty(value = "是否有效，0已删除，1有效")
    private Boolean valid;

    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    private static final long serialVersionUID = 1L;
}
