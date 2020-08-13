package com.cwh.api;

import com.cwh.entity.SpecGroup;
import com.cwh.entity.SpecList;
import com.cwh.entity.SpecParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("spec")
public interface SpecClient {
    @GetMapping(value = "groups/{cid}",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询商品分类规格组")
    @ApiImplicitParam(name = "cid",value = "商品分类id",required = true)
    public ResponseEntity<List<SpecGroup>> searchSpecGroupByCategoruId(@PathVariable("cid") Long categoryId);

    @GetMapping(value = "params",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询商品分类参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid",value = "规格分类id",required = false),
            @ApiImplicitParam(name = "cid",value = "商品分类id",required = false)
    })
    public ResponseEntity<List<SpecParam>> searchSpecParamsBySpecGroupId(
            @RequestParam(value = "gid",required = false)Long groupId,
            @RequestParam(value = "cid",required = false) Long categoryId);

    @PostMapping("/group")
    @ApiOperation(value = "保存规格分组")
    public ResponseEntity<String> saveGroup(@RequestBody SpecGroup specGroup);


    @PutMapping("/group")
    @ApiOperation(value = "修改规格分组")
    public ResponseEntity<String> editGroup(@RequestBody SpecGroup specGroup);

    @PostMapping("param")
    @ApiOperation(value = "保存规格参数")
    public ResponseEntity<String> saveParams(@RequestBody SpecParam specParam);

    @PutMapping("param")
    @ApiOperation(value = "修改规格参数")
    public ResponseEntity<String> editParams(@RequestBody SpecParam specParam);


    @DeleteMapping("group/{id}")
    @ApiOperation("删除规格组")
    @ApiImplicitParam(name = "id",value = "规格主键",required = true)
    public ResponseEntity<String> deleteGroup(@PathVariable("id") Long id);


    @DeleteMapping("param/{id}")
    @ApiOperation("删除规格参数")
    @ApiImplicitParam(name = "id",value = "规格参数主键",required = true)
    public ResponseEntity<String> deleteParams(@PathVariable("id") Long id);

    @GetMapping(value = "specification/{cid}",produces = "application/json;charset=UTF-8")
    @ApiImplicitParam(name = "categoryId",value = "分类id",required = true,type = "Long")
    @ApiOperation("获取规格参数模板")
    public ResponseEntity<SpecList> getSpecificationByCategroupId(@PathVariable("cid") Long categoryId);

    @GetMapping(value = "specParams/{cids}",produces = "application/json;charset=UTF-8")
    @ApiImplicitParam(name = "cids",value = "分类id集合",required = true,type = "List<Long>")
    @ApiOperation("根据分类id集合获取规格参数")
    public ResponseEntity<List<SpecParam>> getSpecParamsByCidList(@PathVariable("cids") List<Long> cids);
}
