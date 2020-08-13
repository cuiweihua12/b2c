package com.cwh.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description: es 存储实体  cuiweihua自定义修改库
 * @author: cuiweihua
 * @create: 2020-08-06 15:10
 */
@Document(indexName = "goodschw",type = "docs",shards = 1,replicas = 0)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "elasticsearch搜索引擎信息存储")
public class GoodsCWH {
    /**
     *  spuId
     */
    @Id
    private Long id;


    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text,analyzer = "keyword")
    private String brand;

    @Field(type = FieldType.Text,analyzer = "keyword")
    private String category;

    /**
     * 卖点
     */
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 1级分类id
     */
    private Long cid1;

    /**
     * 2级分类id
     */
    private Long cid2;

    /**
     * 3级分类id
     */
    private Long cid3;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 价格
     */
    @Field(type = FieldType.Long)
    private List<Long> price;

    /**
     * sku信息的json结构
     */
    @Field(type = FieldType.Keyword, index = false)
    private String skus;

    /**
     * 可搜索的规格参数，key是参数名，值是参数值
     */
    private Map<String, Object> specs;
}
