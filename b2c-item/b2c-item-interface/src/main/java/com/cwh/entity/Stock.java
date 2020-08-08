package com.cwh.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 库存表，代表库存，秒杀库存等信息(TbStock)实体类
 *
 * @author makejava
 * @since 2020-07-29 09:27:12
 */
@Table(name = "tb_stock")
@Data
public class Stock implements Serializable {
    private static final long serialVersionUID = -86853975845989855L;
    /**
     * 库存对应的商品sku id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skuId;
    /**
     * 可秒杀库存
     */
    private Integer seckillStock;
    /**
     * 秒杀总数量
     */
    private Integer seckillTotal;
    /**
     * 库存数量
     */
    private Integer stock;


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Integer getSeckillTotal() {
        return seckillTotal;
    }

    public void setSeckillTotal(Integer seckillTotal) {
        this.seckillTotal = seckillTotal;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}
