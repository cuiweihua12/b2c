package com.cwh.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Data
@Document(indexName = "params",type = "docs",shards = 1,replicas = 0)
public class SpecParamBo implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
    private Long cid;

    @ApiModelProperty(value = "分组id")
    private Long groupId;

    /**
     * 参数名
     */
    @ApiModelProperty(value = "参数名")
    @Column(name = "`name`")
    @Field(type = FieldType.Text,analyzer = "keyword")
    private String name;

    /**
     * 是否是数字类型参数，true或false
     */
    @ApiModelProperty(value = "是否是数字类型参数，true或false")
    @Column(name = "`numeric`")
    private Boolean numeric;

    /**
     * 数字类型参数的单位，非数字类型可以为空
     */
    @ApiModelProperty(value = "数字类型参数的单位，非数字类型可以为空")
    private String unit;

    /**
     * 是否是sku通用属性，true或false
     */
    @ApiModelProperty(value = "是否是sku通用属性，true或false")
    private Boolean generic;

    /**
     * 是否用于搜索过滤，true或false
     */
    @ApiModelProperty(value = "是否用于搜索过滤，true或false")
    private Boolean searching;

    /**
     * 数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0
     */
    @ApiModelProperty(value = "数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0")
    private String segments;

    private static final long serialVersionUID = 1L;
}
