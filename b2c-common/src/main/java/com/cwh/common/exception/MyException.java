package com.cwh.common.exception;

import com.cwh.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @program: b2c
 * @description: 自定义异常类
 * @author: cuiweihua
 * @create: 2020-07-14 17:09
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends RuntimeException{
    ExceptionEnums exceptionEnums;
}
