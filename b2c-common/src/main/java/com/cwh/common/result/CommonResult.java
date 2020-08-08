package com.cwh.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResult {
    RESULT_ERROR("操作失败"),
    RESULT_SUCCESS("操作成功");
    private String msg;
}
