package com.cwh.common.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @program: b2c
 * @description: 异常信息扩展
 * @author: cuiweihua
 * @create: 2020-07-14 17:26
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        //获取自定义异常信息
        Map<String,Object> hashMap = (Map<String, Object>) webRequest.getAttribute("ext", 0);
        map.put("ext",hashMap);
        if (!StringUtils.isEmpty(hashMap.get("msg"))){
            //将默认信息替换为自定义信息
            map.put("message",hashMap.get("msg"));
        }
        return map;
    }

    /* @Override
    //2.3.1 boot 版本使用，因与es版本不兼容后降boot版本弃用
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, options);
        //获取自定义异常信息
        Map<String,Object> hashMap = (Map<String, Object>) webRequest.getAttribute("ext", 0);
        //将自定义异常信息放入
        map.put("ext",hashMap);
        if (!StringUtils.isEmpty(hashMap.get("msg"))){
            //将默认信息替换为自定义信息
            map.put("message",hashMap.get("msg"));
        }
        return map;
    }*/
}
