package com.cwh.controller;

import com.cwh.common.enums.ExceptionEnums;
import com.cwh.common.exception.MyException;
import com.cwh.entity.Category;
import com.cwh.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description: 商品分类控制层
 * @author: cuiweihua
 * @create: 2020-07-15 19:03
 */
@RestController
@RequestMapping("/category")
@Api(tags = "商品分类管理相关")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("list")
    @ApiOperation(value = "商品分类查询",notes = "参数pid，非必传，默认为0")
    @ApiImplicitParam(name = "pid",value = "父级id",required = false,defaultValue = "0")
    @ApiResponse(code = 400,message = "列表为空")
    public ResponseEntity<List<Category>> getCategoryByPid(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        List<Category> list = service.queryCategoryByPid(pid);
        if (list.isEmpty()){
            throw  new MyException(ExceptionEnums.CATEGORY_ISNULL);
        }
        return ResponseEntity.ok(list);
    }

    /**
    *@Description: 删除商品分类
    *@Param: [id]
    *@return: org.springframework.http.ResponseEntity<java.lang.Integer>
    *@Author: cuiweihua
    *@date: 2020/7/15
    */
    @DeleteMapping("/delById")
    @ApiOperation(value = "删除分类")
    @ApiImplicitParam(name = "id",value = "id",defaultValue = "0",required = true)
    @ApiResponse(code = 400,message = "id不能为空或零")
    public ResponseEntity<String> removeCategoryById(@RequestParam(value = "id",defaultValue = "0") Long id){
        if (id == null || id == 0){
            throw new MyException(ExceptionEnums.ID_CATNOT_ISNULL);
        }

        return ResponseEntity.status(200).body(service.removeCategoryById(id));
    }

    /**
    *@Description: 修改商品分类
    *@Param: [category]
    *@return: org.springframework.http.ResponseEntity<java.lang.String>
    *@Author: cuiweihua
    *@date: 2020/7/15
    */
    @PutMapping(value = "editCategoryById",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "修改商品分类",notes = "参数分类对象")
    public ResponseEntity<String> editCategoryById(@RequestBody Category category){
        if (category.getId() == null || category.getId() == 0){
            throw new MyException(ExceptionEnums.ID_CATNOT_ISNULL);
        }
        service.editCategoryById(category);
        return ResponseEntity.ok("Success!");
    }


    /**
    *@Description: 保存商品分类
    *@Param: [category]
    *@return: org.springframework.http.ResponseEntity<java.lang.String>
    *@Author: cuiweihua
    *@date: 2020/7/15
    */
    @PostMapping("saveCategory")
    @ApiOperation(value = "保存商品分类",notes = "参数分类对象")
    public ResponseEntity<Category> saveCategory(@RequestBody Category category){

        return ResponseEntity.ok(service.saveCategory(category));
    }

    /**
     *@Description: 通过品牌id查询商品分类信息
     *@Param: [id]
     *@return: org.springframework.http.ResponseEntity<com.cwh.entity.Category>
     *@Author: cuiweihua
     *@date: 2020/7/16
     */
    @GetMapping("/bid")
    @ApiOperation(value = "根据品牌id查询商品信息",notes = "参数分类对象")
    public ResponseEntity<Map<String,Object>> getCateGoryById(@RequestParam("id") Long id){
        if (id == null){
            throw  new MyException(ExceptionEnums.ID_CATNOT_ISNULL);
        }
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @GetMapping("queryCategory/{ids}")
    @ApiImplicitParam(name = "ids",value = "分类id集合",type = "List/Array")
    public ResponseEntity<List<Category>> queryCategory(@PathVariable("ids") List<Long> ids){
        return ResponseEntity.ok(service.queryCategory(ids));
    }
    /*@GetMapping("/bid")
    public ResponseEntity<Category> getCateGoryById(@RequestParam("id") Long id){
        if (id == null){
            throw  new MyException(ExceptionEnums.ID_CATNOT_ISNULL);
        }
        return ResponseEntity.ok(service.getCategoryById(id));
    }*/

    @GetMapping("allCategory")
    @ApiOperation(value = "获取所有商品分类")
    public ResponseEntity<List<Category>> queryCategoryAll(){
        return ResponseEntity.ok(service.queryCategoryAll());
    }
}
