package com.cwh.service;

import com.cwh.bo.SpuBo;
import com.cwh.common.utils.PageResult;
import com.cwh.entity.Sku;
import com.cwh.entity.Spu;
import com.cwh.entity.SpuDetail;

import java.util.HashMap;
import java.util.List;

public interface GoodsService {
    PageResult<Spu> list(String sortBy, Boolean desc, Integer page, Integer pageNum, String search, Integer saleable);

    Integer saveGoods(HashMap<String ,Object> map) throws InstantiationException, IllegalAccessException;

    void deleteGoods(Long id);

    void editGoodsSaleable(Long id, Boolean saleable);

    SpuDetail querySpuDetailBySpuId(Long pid);

    List<Sku> querySkusBySpuId(Long pid);

    void editGoods(SpuBo spuBo);
}
