package com.cwh.api;

import com.cwh.entity.Category;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-06 15:32
 */
@RequestMapping("category")
public interface CategoryClient {

    @GetMapping("list")
    @ApiOperation(value = "商品分类查询",notes = "参数pid，非必传，默认为0")
    @ApiImplicitParam(name = "pid",value = "父级id",required = false,defaultValue = "0")
    @ApiResponse(code = 400,message = "列表为空")
    public ResponseEntity<List<Category>> getCategoryByPid(@RequestParam(value = "pid",defaultValue = "0") Long pid);

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
    public ResponseEntity<String> removeCategoryById(@RequestParam(value = "id",defaultValue = "0") Long id);

    /**
     *@Description: 修改商品分类
     *@Param: [category]
     *@return: org.springframework.http.ResponseEntity<java.lang.String>
     *@Author: cuiweihua
     *@date: 2020/7/15
     */
    @PutMapping(value = "editCategoryById",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "修改商品分类",notes = "参数分类对象")
    public ResponseEntity<String> editCategoryById(@RequestBody Category category);


    /**
     *@Description: 保存商品分类
     *@Param: [category]
     *@return: org.springframework.http.ResponseEntity<java.lang.String>
     *@Author: cuiweihua
     *@date: 2020/7/15
     */
    @PostMapping("saveCategory")
    @ApiOperation(value = "保存商品分类",notes = "参数分类对象")
    public ResponseEntity<Category> saveCategory(@RequestBody Category category);

    /**
     *@Description: 通过品牌id查询商品分类信息
     *@Param: [id]
     *@return: org.springframework.http.ResponseEntity<com.cwh.entity.Category>
     *@Author: cuiweihua
     *@date: 2020/7/16
     */
    @GetMapping("/bid")
    @ApiOperation(value = "根据品牌id查询商品信息",notes = "参数分类对象")
    public ResponseEntity<Map<String,Object>> getCateGoryById(@RequestParam("id") Long id);

    @GetMapping("queryCategory/{ids}")
    @ApiImplicitParam(name = "ids",value = "分类id集合",type = "List/Array")
    public ResponseEntity<List<Category>> queryCategory(@PathVariable("ids") List<Long> ids);

    @GetMapping("allCategory")
    @ApiOperation(value = "获取所有商品分类")
    public ResponseEntity<List<Category>> queryCategoryAll();
}
