package com.cwh.service;

import com.cwh.pojo.GoodsCWH;
import com.cwh.pojo.Pages;
import com.cwh.pojo.SearchResult;

public interface GoodsCWHService {
    SearchResult<GoodsCWH> searchGoods(Pages page);

    void saveGoods(GoodsCWH goods);
}
