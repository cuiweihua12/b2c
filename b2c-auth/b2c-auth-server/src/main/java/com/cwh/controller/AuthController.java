package com.cwh.controller;

import com.cwh.common.utils.CookieUtils;
import com.cwh.config.JwtConfig;
import com.cwh.jopo.UserInfo;
import com.cwh.service.AuthService;
import com.cwh.utils.JwtUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-26 21:31
 */
@RestController
@Api(tags = "授权管理")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/login")
    @ApiOperation("登录授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true,type = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,type = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "授权成功"),
            @ApiResponse(code = 401,message = "授权失败"),
            @ApiResponse(code = 500,message = "服务器内部错误")
    })
    public ResponseEntity<Void> login(@RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password,
                                      HttpServletRequest request, HttpServletResponse response){

        String token = authService.login(username,password);

        if (StringUtils.isEmpty(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //将token放入浏览器
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token,jwtConfig.getExpire(),true);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("B2C_TOKEN") String token,HttpServletRequest request,HttpServletResponse response){

        try {
            //解析token
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //生成新token
            String newtoken = JwtUtils.generateToken(infoFromToken, jwtConfig.getPrivateKey(), jwtConfig.getCookieMaxAge());
            //设置浏览器新token
            CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),newtoken,jwtConfig.getExpire(),true);
            return ResponseEntity.ok().body(infoFromToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

}
