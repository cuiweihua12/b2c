package com.cwh.bo;

import com.cwh.entity.Spu;
import com.cwh.entity.SpuDetail;
import lombok.Data;

import java.util.List;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-30 16:07
 */
@Data
public class SpuBo extends Spu {

    private SpuDetail spuDetail;

    private List<SkuBo> skus;
}
