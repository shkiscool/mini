package com.dazhao.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.dazhao.common.FailureResponseUtil;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.service.UserService;
import com.github.pagehelper.util.StringUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    private static final String HREADER_TOKEN_PARAM = "authorization";


    /**
     * 进入controller方法之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        String authorization = request.getHeader(HREADER_TOKEN_PARAM);
        try {
            boolean verify = JWTUtil.verify(authorization);
            if (!verify) {
                FailureResponseUtil.failureResponse(response, JSONObject.toJSONString(ResponseUtil.unlogin()), HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            int userId = JWTUtil.getUserInfo(authorization);
            boolean isPermission = isPermission(request, userId);
            if (!isPermission) {
                FailureResponseUtil.failureResponse(response, JSONObject.toJSONString(ResponseUtil.unauthz()), HttpStatus.FORBIDDEN.value());
                return false;
            }
        } catch (Exception e) {
            FailureResponseUtil.failureResponse(response, JSONObject.toJSONString(ResponseUtil.serious()), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }
        return true;
    }

    /**
     * 调用完controller之后 视图渲染之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 整个完成之后，通常用于资源清理
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private boolean isPermission(HttpServletRequest request, Integer usreId) {
        List<String> urlList = userService.getUserPermissionList(usreId);
        if (urlList == null) {
            return false;
        }
        String servletPath = request.getServletPath();
        if (StringUtil.isEmpty(servletPath)) {
            servletPath = request.getPathInfo();
        }
        for (String url : urlList) {
            boolean isTure = servletPath.startsWith(url);
            if (isTure) {
                return true;
            }
        }
        return false;
    }

}
