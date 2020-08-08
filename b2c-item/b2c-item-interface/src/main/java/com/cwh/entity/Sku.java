package com.cwh.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8(TbSku)实体类
 *
 * @author makejava
 * @since 2020-07-28 21:23:23
 */
@Data
@Table(name = "tb_sku")
public class Sku implements Serializable {
    private static final long serialVersionUID = 552709928663670573L;
    /**
     * sku id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KeySql(useGeneratedKeys = true)
    private Long id;
    /**
     * spu id
     */
    private Long spuId;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品的图片，多个图片以‘,’分割
     */
    private String images;
    /**
     * 销售价格，单位为分
     */
    private Long price;
    /**
     * 特有规格属性在spu属性模板中的对应下标组合
     */
    private String indexes;
    /**
     * sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
     */
    private String ownSpec;
    /**
     * 是否有效，0无效，1有效
     */
    private Boolean enable;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date lastUpdateTime;

    @Transient
    private Integer stock;


}
