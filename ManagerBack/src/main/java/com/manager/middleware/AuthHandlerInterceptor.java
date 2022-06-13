package com.manager.middleware;

import com.manager.constant.SessionFields;
import com.manager.util.SessionChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthHandlerInterceptor implements HandlerInterceptor {

    /**
     * 拦截器 发生在所有请求之前
     * 用于用户登录状态的提前检验与拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "*");
        // 以上是为了跨域进行的一些设置
        // 直接放行 OPTIONS请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }
        // 检查 session
        SessionChecker sessionChecker = SessionChecker.checkSession(request);
        if (sessionChecker.isAuthed()) {
            request.setAttribute(SessionFields.USERNAME, sessionChecker.getID());
            // 返回 true 进入 controller层
            return true;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // 返回 false 不进入 controller层 直接拒绝本次请求
            return false;
        }
    }
}
