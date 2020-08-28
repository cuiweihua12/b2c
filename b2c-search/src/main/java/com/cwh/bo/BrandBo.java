package com.cwh.bo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * 品牌表，一个品牌下有多个商品（spu），一对多关系(TbBrand)实体类
 *
 * @author makejava
 * @since 2020-07-16 12:00:03
 */
@Data
@Document(indexName = "brands",type = "docs",shards = 1,replicas = 0)
public class BrandBo implements Serializable {
    private static final long serialVersionUID = 289679571676927727L;
    /**
     * 品牌id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KeySql(useGeneratedKeys = true)
    private Long id;
    /**
     * 品牌名称
     */
    @Field(type = FieldType.Text,analyzer = "keyword")
    private String name;
    /**
     * 品牌图片地址
     */
    @Field(type = FieldType.Text,analyzer = "keyword")
    private String image;
    /**
     * 品牌的首字母
     */
    private String letter;


}
