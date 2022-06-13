package com.manager.config;

import com.manager.middleware.AuthHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     * 用于用户登录状态的拦截与认证
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/", "/student/api/excelDownload", "/login", "/logout", "/role", "/hello", "/makeSession/**", "/css/**", "/js/**", "/fonts/**", "images/**");
    }
}
