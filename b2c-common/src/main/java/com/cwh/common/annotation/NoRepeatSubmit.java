package com.cwh.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//使用在方法上
@Retention(RetentionPolicy.RUNTIME)//运行时有效
/**
 * @auth cuiweihua
 * @功能描述 防止重复发送请求
 * @date 2020/7/19
 */
public @interface NoRepeatSubmit {
}
