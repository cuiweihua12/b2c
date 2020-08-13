package com.cwh.service;

import com.cwh.common.utils.PageResult;
import com.cwh.pojo.Goods;
import com.cwh.pojo.Pages;

public interface GoodsService {
    PageResult<Goods> searchGoods(Pages page);
}
