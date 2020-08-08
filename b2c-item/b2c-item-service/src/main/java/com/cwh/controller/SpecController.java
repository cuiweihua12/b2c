package com.cwh.controller;

import com.cwh.entity.SpecGroup;
import com.cwh.entity.SpecList;
import com.cwh.entity.SpecParam;
import com.cwh.service.SpecGroupService;
import com.cwh.service.SpecParamService;
import com.cwh.service.SpecificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: b2c
 * @description: 规格相关
 * @author: cuiweihua
 * @create: 2020-07-23 13:45
 */
@RestController
@RequestMapping("spec")
@Api(tags = "规格相关接口")
public class SpecController {

    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;

    @Autowired
    private SpecificationService specificationService;


    @GetMapping(value = "groups/{cid}",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询商品分类规格组")
    @ApiImplicitParam(name = "cid",value = "商品分类id",required = true)
    public ResponseEntity<List<SpecGroup>> searchSpecGroupByCategoruId(@PathVariable("cid") Long categoryId){
        return ResponseEntity.ok(specGroupService.searchSpecGroupByCategoruId(categoryId));
    }

    @GetMapping(value = "params",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询商品分类参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid",value = "规格分类id",required = false),
            @ApiImplicitParam(name = "cid",value = "商品分类id",required = false)
    })
    public ResponseEntity<List<SpecParam>> searchSpecParamsBySpecGroupId(@RequestParam(value = "gid",required = false)Long groupId,
                                                                         @RequestParam(value = "cid",required = false) Long categoryId){

        return ResponseEntity.ok(specGroupService.searchSpecParams(groupId,categoryId));
    }

    @PostMapping("/group")
    @ApiOperation(value = "保存规格分组")
    public ResponseEntity<String> saveGroup(@RequestBody SpecGroup specGroup){
        specGroupService.saveGroup(specGroup);
        return ResponseEntity.ok("success");
    }
    @PutMapping("/group")
    @ApiOperation(value = "修改规格分组")
    public ResponseEntity<String> editGroup(@RequestBody SpecGroup specGroup){
        specGroupService.saveGroup(specGroup);
        return ResponseEntity.ok("success");
    }

    @PostMapping("param")
    @ApiOperation(value = "保存规格参数")
    public ResponseEntity<String> saveParams(@RequestBody SpecParam specParam){
        specParamService.saveParams(specParam);
        return ResponseEntity.ok().build();
    }

    @PutMapping("param")
    @ApiOperation(value = "修改规格参数")
    public ResponseEntity<String> editParams(@RequestBody SpecParam specParam){
        specParamService.saveParams(specParam);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("group/{id}")
    @ApiOperation("删除规格组")
    @ApiImplicitParam(name = "id",value = "规格主键",required = true)
    public ResponseEntity<String> deleteGroup(@PathVariable("id") Long id){
        specGroupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("param/{id}")
    @ApiOperation("删除规格参数")
    @ApiImplicitParam(name = "id",value = "规格参数主键",required = true)
    public ResponseEntity<String> deleteParams(@PathVariable("id") Long id){
        specParamService.deleteParams(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "specification/{cid}",produces = "application/json;charset=UTF-8")
    @ApiImplicitParam(name = "categoryId",value = "分类id",required = true,type = "Long")
    @ApiOperation("获取规格参数模板")
    public ResponseEntity<SpecList> getSpecificationByCategroupId(@PathVariable("cid") Long categoryId){
        return ResponseEntity.ok(specGroupService.getSearchGroupParmas(categoryId));
    }
}
