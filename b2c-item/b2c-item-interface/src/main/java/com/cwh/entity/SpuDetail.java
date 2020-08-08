package com.cwh.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (TbSpuDetail)实体类
 *
 * @author makejava
 * @since 2020-07-28 19:38:34
 */
@Data
@Table(name = "tb_spu_detail")
public class SpuDetail implements Serializable {
    private static final long serialVersionUID = -53468999583246718L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spuId;
    /**
     * 商品描述信息
     */
    private String description;
    /**
     * 通用规格参数数据
     */
    private String genericSpec;
    /**
     * 特有规格参数及可选值信息，json格式
     */
    private String specialSpec;
    /**
     * 包装清单
     */
    private String packingList;
    /**
     * 售后服务
     */
    private String afterService;




}
