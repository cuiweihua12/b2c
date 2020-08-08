package com.cwh.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商品分类和品牌的中间表，两者是多对多关系(TbCategoryBrand)实体类
 *
 * @author makejava
 * @since 2020-07-16 13:39:24
 */
@Data
@Table(name = "tb_category_brand")
public class CategoryBrand implements Serializable {
    private static final long serialVersionUID = -69991976051543478L;
    /**
     * 商品类目id
     */
    private Long categoryId;
    /**
     * 品牌id
     */
    private Long brandId;



}
