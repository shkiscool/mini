package com.dazhao.common.result;

import java.io.Serializable;
import lombok.Data;

@Data
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = -8896679232700418515L;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private T errorMsg;
    /**
     * 响应数据
     */
    private T data;
}
