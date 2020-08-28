package com.cwh.api;

import com.cwh.pojo.User;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-26 21:33
 */
@RestController
@Api(tags = "用户管理模块")
public interface UserApi {


    @GetMapping("/check/{data}/{type}")
    @ApiOperation("校验用户名，或手机号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data",value = "用户名或手机号码",required = true,type = "String"),
            @ApiImplicitParam(name = "type",value = "当为1时data为用户名，2时为手机号",required = true,type = "Integer")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正常"),
            @ApiResponse(code = 400,message = "参数异常"),
            @ApiResponse(code = 500,message = "服务器内存错误")
    })
    public Boolean check(@PathVariable("data") String data,
                                         @PathVariable(value = "type") Integer type);



    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true,type = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,type = "String"),
            @ApiImplicitParam(name = "phone",value = "手机号码",required = true,type = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正常"),
            @ApiResponse(code = 400,message = "参数异常"),
            @ApiResponse(code = 500,message = "服务器内存错误")
    })
    public Void register(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "phone") String phone
    );

    @ApiOperation("根据用户名和密码查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true,type = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,type = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求正常"),
            @ApiResponse(code = 400,message = "参数异常"),
            @ApiResponse(code = 500,message = "服务器内存错误")
    })
    @GetMapping("/query")
    public User queryByUsernameOrPassword(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    );
}
