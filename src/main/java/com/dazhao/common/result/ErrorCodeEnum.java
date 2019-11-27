package com.dazhao.common.result;

public enum ErrorCodeEnum {
    SUCCESS("1", "成功"),

    FAILE("-1", "失败"),

    PARAM_ERROR("1401", "参数错误"),

    IMAGE_UPLOAD_DEFEATED("-1", "图片上传失败"),

    SIGNATURE_FAILE("-1", "签署请求失败"),

    IMAGE_UPLOAD_TYPE("1401", "请上传图片文件类型"),

    LOGIN_ERROR("1501", "用户名/密码错误"),

    USER_EXIST("1401", "已经被注册了账号名称"),

    LOGIN_EXPIRES("1502", "用户登录过期请重新登录"),

    SYSTEM_ERROR("1503", "系统内部错误"),

    NO_POWER("1504", "无权操作"),

    SYSTEM_BUSY("9999", "系统繁忙，请稍后再试....");

    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }


    public String getErrorMsg() {
        return errorMsg;
    }


    ErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ErrorCodeEnum{"
                + "errorCode=" + errorCode
                + ", errorMsg='" + errorMsg + '\''
                + '}';
    }
}
