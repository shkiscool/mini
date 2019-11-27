package com.dazhao.common;

import com.dazhao.common.result.CommonResult;
import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class CustomException extends RuntimeException {

    private CommonResult commonResult;

    public CustomException(CommonResult commonResult) {
        this.commonResult = commonResult;
    }
}
