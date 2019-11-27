package com.dazhao.common;

import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局捕获异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局捕获异常处理方法
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(Exception.class)
    public CommonResult exceptionHandler(Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        CommonResult serious = ResponseUtil.serious();
        log.error("报错内容:", e);
        return serious;
    }

    /**
     * 捕获自定义异常处理方法
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(value = CustomException.class)
    CommonResult handelCustomException(CustomException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("报错内容:", e);
        return e.getCommonResult();
    }

    /**
     * 捕获上传文件大小异常
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommonResult handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseUtil.fail("-1", "文件大小超出10MB限制, 请压缩或降低文件质量!");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), bindingResult.getFieldErrors().get(0).getDefaultMessage());
    }


}
