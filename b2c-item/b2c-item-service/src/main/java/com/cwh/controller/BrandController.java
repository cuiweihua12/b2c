package com.cwh.controller;

import com.cwh.common.annotation.NoRepeatSubmit;
import com.cwh.common.utils.PageResult;
import com.cwh.entity.Brand;
import com.cwh.service.BrandService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description: 品牌控制层
 * @author: cuiweihua
 * @create: 2020-07-16 12:07
 */
@RestController
@RequestMapping("/brand")
@Api(tags = "品牌管理相关接口")
@CacheConfig(cacheNames = "brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("list")
    //两秒内不可发送重复请求 自定义注解
    @NoRepeatSubmit
    @ApiOperation(value = "查询品牌集合",tags = "带分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数",defaultValue = "1",paramType = "Integer"),
            @ApiImplicitParam(name = "pageNum",value = "每页行数",defaultValue = "5",paramType = "Integer"),
            @ApiImplicitParam(name = "sortBy",value = "排序字段",required = false,paramType = "String"),
            @ApiImplicitParam(name = "sortDesc",value = "排序方式",defaultValue = "false",paramType = "Boolean")
    })
    public ResponseEntity<PageResult<Brand>> brandList(Brand brand,
                                                       @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "pageNum",defaultValue = "5") Integer pageNum,
                                                       @RequestParam(value = "sortBy",required = false) String sortBy,
                                                       @RequestParam(value = "sortDesc",required = false,defaultValue = "false")Boolean sortDesc){
        return ResponseEntity.ok(brandService.list(brand,page,pageNum,sortBy,sortDesc));
    }

    @GetMapping("all")
    @ApiOperation("查询所有品牌")
    public ResponseEntity<List<Brand>> getBrandAll(){
        return ResponseEntity.ok(brandService.searchBrandAll());
    }

    @PostMapping
    @ApiOperation(value = "保存品牌信息")
    public ResponseEntity<Brand> saveBrand(@RequestBody Brand brand){
        return ResponseEntity.ok(brandService.saveBrand(brand));
    }

    @PutMapping
    @ApiOperation("修改品牌信息")
    public ResponseEntity<Brand> eidtBrand(@RequestBody Brand brand){
        return ResponseEntity.ok(brandService.saveBrand(brand));
    }

    @ApiOperation("删除品牌信息")
    @ApiImplicitParam(name = "id",value = "id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable("id") Long id){

        return ResponseEntity.ok( brandService.deleteBrand(id).getMsg());
    }

    @GetMapping("criteriaQuery")
    public ResponseEntity<List<Brand>> criteriaQuery(Brand brand){

        return ResponseEntity.ok(brandService.criteraQuery(brand));
    }

    @GetMapping("queryById")
    @ApiOperation("根据id查询品牌信息,同时返回分类信息")
    public ResponseEntity<Map<String,Object>> queryById(Brand brand){

        return ResponseEntity.ok(brandService.queryById(brand));
    }

    @GetMapping("queryByBrandId/{bid}")
    @ApiOperation("根据id查询品牌信息")
    @ApiImplicitParam(name = "bid",value = "品牌id",type = "Long")
    public ResponseEntity<Brand> queryByBrandId(@PathVariable("bid") Long bid){
        return ResponseEntity.ok(brandService.queryByBrandId(bid));
    }

    @GetMapping("queryBrandByCategoryId/{cId}")
    @ApiOperation(value = "根据分类id查询品牌信息")
    @ApiParam(name = "cateGoryId",value = "商品分类id",required = true)
    public ResponseEntity<List<Brand>> queryBrandByCategoryId(@PathVariable("cId") Long cateGoryId){
        return ResponseEntity.ok(brandService.queryBrandByCategoryId(cateGoryId));
    }

    @GetMapping("queryByIdList/{ids}")
    @ApiOperation(("根据id集合查询品牌信息"))
    public ResponseEntity<List<Brand>> queryByIdList(@PathVariable("ids") List<Long> ids){
        return ResponseEntity.ok(brandService.queryByIdList(ids));
    }
}
