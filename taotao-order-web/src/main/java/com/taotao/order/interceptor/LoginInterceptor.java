package com.taotao.order.interceptor;


import com.taotao.common.interceptor.LoginInterceptorHandle;
import com.taotao.sso.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginInterceptor implements HandlerInterceptor {

    //引入SSO里，UserLoginService 服务
    @Autowired
    private UserLoginService userLoginService;

    @Value("${LOGIN_URL}")
    private String LOGIN_URL;

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        System.out.println("拦截起作用");
        boolean b = LoginInterceptorHandle.preHandle(userLoginService, LOGIN_URL, TT_TOKEN_KEY, request, response, handler);

        return b;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
