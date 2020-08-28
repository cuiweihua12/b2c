package com.cwh.controller;

import com.cwh.bo.SpuBo;
import com.cwh.common.utils.PageResult;
import com.cwh.entity.Sku;
import com.cwh.entity.Spu;
import com.cwh.entity.SpuDetail;
import com.cwh.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @program: b2c
 * @description: 商品相关
 * @author: cuiweihua
 * @create: 2020-07-23 13:09
 */
@RestController
@RequestMapping("goods")
@Api(tags = "商品相关接口")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;



    @GetMapping(value = "/list",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询商品信息带分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数",defaultValue = "1",paramType = "Integer"),
            @ApiImplicitParam(name = "pageNum",value = "每页行数",defaultValue = "5",paramType = "Integer"),
            @ApiImplicitParam(name = "sortBy",value = "排序字段",required = false,paramType = "String"),
            @ApiImplicitParam(name = "desc",value = "排序方式",defaultValue = "false",paramType = "Boolean"),
            @ApiImplicitParam(name = "saleable",value = "是否上架，0下架，1上架,2全部",paramType = "Integer")
    })
    public ResponseEntity<PageResult<Spu>> list(@RequestParam(value = "sortBy",required = false,defaultValue = "id") String sortBy,
                                           @RequestParam(value = "desc",required = false,defaultValue = "false") Boolean desc,
                                           @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                           @RequestParam(value = "pageNum",required = false,defaultValue = "5") Integer pageNum,
                                           @RequestParam(value = "search",required = false)String search,
                                           @RequestParam(value = "saleable",required = false,defaultValue = "null")Integer saleable){
        return ResponseEntity.ok(goodsService.list(sortBy,desc,page,pageNum,search,saleable));
    }

    @PostMapping
    @ApiOperation(value = "保存商品")
    public ResponseEntity<String> saveGoods(@RequestBody HashMap<String ,Object> map) throws IllegalAccessException, InstantiationException {
        goodsService.saveGoods(map);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "删除商品")
    public ResponseEntity<String> delGoods(@RequestParam("id") Long id){
        goodsService.deleteGoods(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("saleable")
    @ApiOperation(value = "修改商品上下架状态")
    public ResponseEntity<String> editGoodsSaleable(@RequestBody Spu spu){
        goodsService.editGoodsSaleable(spu.getId(),spu.getSaleable());
        return ResponseEntity.ok().build();
    }

    @GetMapping("spuDetail/{pid}")
    @ApiOperation(value = "根据spuid查询spu详情")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("pid") Long pid){

        return ResponseEntity.ok(goodsService.querySpuDetailBySpuId(pid));
    }

    @GetMapping("skus/{pid}")
    @ApiOperation(value = "根据spuid查询sku")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@PathVariable("pid") Long pid){
        return ResponseEntity.ok(goodsService.querySkusBySpuId(pid));
    }

    @PutMapping
    @ApiOperation(value = "修改商品信息")
    public ResponseEntity<Void> eidtGoods(@RequestBody SpuBo spuBo){
        goodsService.editGoods(spuBo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("querySpu/{pid}")
    @ApiOperation(value = "根据主键查询spu信息")
    @ApiImplicitParam(name = "pid",value = "主键",required = true,type = "Long")
    public ResponseEntity<Spu> querySpuById(@PathVariable("pid") Long pid){

        return ResponseEntity.ok(goodsService.querySpuById(pid));
    }

}
