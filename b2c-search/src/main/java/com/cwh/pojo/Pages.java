package com.cwh.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-10 09:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "商品分页查询信息")
public class Pages {

    @ApiModelProperty(name = "key",value = "搜索数据",dataType = "String")
    private String key;

    @ApiModelProperty(name = "page",value = "分页开始",dataType = "Integer")
    private Integer page = 0;

    @ApiModelProperty(name = "size",value = "每页条数",dataType = "Integer")
    private Integer size = 20;

    @ApiModelProperty(name = "sort",value = "排序方式",dataType = "Boolean")
    private Boolean sort;

    @ApiModelProperty(name = "sortBy",value = "排序字段",dataType = "String",example = "price createTime",required = false)
    private String sortBy;
}
