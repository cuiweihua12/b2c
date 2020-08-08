package com.cwh.common.exception;

import com.cwh.common.enums.ExceptionEnums;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @program: b2c
 * @description: 自定义异常处理
 * @author: cuiweihua
 * @create: 2020-07-14 17:03
 */

@ControllerAdvice
public class CommonExceptionHandler {

    /**
     *自定义错误处理
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(MyException.class)
    public String myException(HttpServletRequest request, HttpServletResponse response,MyException m){
        //设置错误状态码
        request.setAttribute("javax.servlet.error.status_code", m.exceptionEnums.getCode());
        HashMap<String, Object> hashMap = new HashMap<>();
        //设置错误自定义错误信息
        hashMap.put("msg",m.exceptionEnums.getMsg());
        //将自动定义信息存入requst
        request.setAttribute("ext",hashMap);
        //错误转发
        return "forward:/error";
    }



}
