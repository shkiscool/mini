package com.dazhao.common.result;


import com.github.pagehelper.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtil {

    /**
     * 成功
     */
    public static CommonResult ok() {
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setSuccess(true);
        return commonResult;
    }

    /**
     * 成功
     *
     * @param data 返回数据
     */
    public static <T> CommonResult<T> ok(T data) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setSuccess(true);
        commonResult.setData(data);
        return commonResult;
    }

    /**
     * 成功
     *
     * @param list 返回数据
     */
    public static CommonResult okList(List list) {
        CommonResult commonResult = new CommonResult<>();
        commonResult.setSuccess(true);
        commonResult.setData(setData(list, null));
        return commonResult;
    }

    /**
     * 成功
     *
     * @param list 返回数据
     */
    public static CommonResult okList(List list, Object object) {
        CommonResult commonResult = new CommonResult<>();
        commonResult.setSuccess(true);
        commonResult.setData(setData(list, object));
        return commonResult;
    }

    private static Map setData(List list, Object object) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", list);
        if (object != null) {
            data.put("object", object);
        }

        if (list instanceof Page) {
            Page page = (Page) list;
            data.put("total", page.getTotal());
            data.put("page", page.getPageNum());
            data.put("limit", page.getPageSize());
            data.put("pages", page.getPages());
        } else {
            if (list == null) {
                data.put("total", 0);
                data.put("page", 0);
                data.put("limit", 0);
                data.put("pages", 0);
            } else {
                data.put("total", list.size());
                data.put("page", 1);
                data.put("limit", list.size());
                data.put("pages", 1);
            }

        }
        return data;
    }

    /**
     * 失败
     */
    public static CommonResult fail() {
        CommonResult commonResult = new CommonResult<>();
        commonResult.setErrorCode(ErrorCodeEnum.FAILE.getErrorCode());
        commonResult.setErrorMsg(ErrorCodeEnum.FAILE.getErrorMsg());
        commonResult.setSuccess(false);
        return commonResult;
    }


    /**
     * 失败
     *
     * @param data 返回数据
     */
    public static <T> CommonResult<T> fail(T data) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setSuccess(false);
        commonResult.setErrorMsg(data);
        return commonResult;
    }

    /**
     * 失败
     *
     * @param errno 错误码
     * @param errmsg 错误信息
     */
    public static CommonResult fail(String errno, String errmsg) {
        CommonResult commonResult = new CommonResult();
        commonResult.setErrorCode(errno);
        commonResult.setErrorMsg(errmsg);
        commonResult.setSuccess(false);
        return commonResult;
    }

    public static CommonResult badArgument() {
        return fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
    }

    public static CommonResult unlogin() {
        return fail(ErrorCodeEnum.LOGIN_EXPIRES.getErrorCode(), ErrorCodeEnum.LOGIN_EXPIRES.getErrorMsg());
    }

    public static CommonResult loginDefeated() {
        return fail(ErrorCodeEnum.LOGIN_ERROR.getErrorCode(), ErrorCodeEnum.LOGIN_ERROR.getErrorMsg());
    }

    public static CommonResult serious() {
        return fail(ErrorCodeEnum.SYSTEM_ERROR.getErrorCode(), ErrorCodeEnum.SYSTEM_ERROR.getErrorMsg());
    }

    public static CommonResult unauthz() {
        return fail(ErrorCodeEnum.NO_POWER.getErrorCode(), ErrorCodeEnum.NO_POWER.getErrorMsg());
    }

    public static CommonResult systemBusy() {
        return fail(ErrorCodeEnum.SYSTEM_BUSY.getErrorCode(), ErrorCodeEnum.SYSTEM_BUSY.getErrorMsg());
    }
}

