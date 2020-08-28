package com.cwh.controller;

import com.cwh.common.enums.ExceptionEnums;
import com.cwh.common.exception.MyException;
import com.cwh.pojo.GoodsCWH;
import com.cwh.pojo.Pages;
import com.cwh.pojo.SearchResult;
import com.cwh.service.GoodsCWHService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-06 10:59
 */
@RestController
@RequestMapping(value = "goods")
@Api(value = "es查询商品信息接口")
public class SearchController {


    @Autowired
    private GoodsCWHService goodsCWHService;

    @GetMapping(value = "page")
    @ApiOperation(value = "分页查询商品")
    public ResponseEntity<SearchResult<GoodsCWH>> searchGoods(@NotNull Pages page){
        SearchResult<GoodsCWH> result = goodsCWHService.searchGoods(page);
        if (result == null){
            throw new MyException(ExceptionEnums.CATEGORY_ISNULL);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/saveGoods")
    @ApiOperation(value = "保存商品")
    public ResponseEntity<Void> saveGoods(@RequestBody GoodsCWH goods){
        goodsCWHService.saveGoods(goods);
        return ResponseEntity.ok().build();
    }


}
