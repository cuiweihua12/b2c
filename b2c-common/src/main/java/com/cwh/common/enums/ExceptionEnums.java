package com.cwh.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-14 17:10
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum  ExceptionEnums {
    SERVICE_BAD(500,"服务发生故障，请稍候重试"),
    PRICE_NOTNULL(400,"价格不能为空"),
    BID_NOTNULL(400,"类别ID不能为空"),
    ID_CATNOT_ISNULL(400,"id不能为空或零"),
    FILE_IS_NOTNULL(400,"文件不能为空"),
    SUCCESS(200,"SUCCESS"),
    CATEGORY_ISNULL(404,"列表为空");
    private Integer code;
    private String msg;


}
