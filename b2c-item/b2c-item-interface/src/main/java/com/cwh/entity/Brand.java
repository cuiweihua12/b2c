package com.cwh.entity;

import com.sun.xml.internal.ws.developer.UsesJAXBContext;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * 品牌表，一个品牌下有多个商品（spu），一对多关系(TbBrand)实体类
 *
 * @author makejava
 * @since 2020-07-16 12:00:03
 */
@Data
@Table(name = "tb_brand")
@ApiModel(description = "品牌实体类")
public class Brand implements Serializable {
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
    private String name;
    /**
     * 品牌图片地址
     */
    private String image;
    /**
     * 品牌的首字母
     */
    private String letter;

    /**
     * 品牌
     */
//    private List<Category> categories;

    private Long[] categories;

}
