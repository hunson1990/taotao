package com.taotao.common.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbUser;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.service.UserLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptorHandle {

    public static void setResponse(HttpServletResponse response, Integer status,
                                   String message, String targetUrl) throws IOException {
        TaotaoResult result1 = TaotaoResult.build(status, message);
        PrintWriter writer = response.getWriter();
        writer.append(result1.toString());
        //重定向到URL
        response.sendRedirect(targetUrl);
    }

    public static boolean preHandle(UserLoginService userLoginService,String LOGIN_URL,String TT_TOKEN_KEY,
                                    HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {


        //从cookie里面获取token
        //如果没有token说明没有登陆
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        LOGIN_URL = LOGIN_URL + "?redirect=" + request.getRequestURL().toString();
        if(token == null){
            LoginInterceptorHandle.setResponse(response,400,"没有登陆",LOGIN_URL);
            return false;
        }
        //根据token获得用户信息LoginInterceptor
        //token不存在，说明登陆失效
        TaotaoResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() != 200){
            //登陆信息不存在，
            LoginInterceptorHandle.setResponse(response,400,"登陆已失效",LOGIN_URL);
            return false;
        }
        //说明已经登陆  --->>> 放行
        //把用户信息放到request里面
        request.setAttribute("USER_INFO",result.getData());
        return true;
    }

}
