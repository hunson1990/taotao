package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@Controller
public class UserLoginController {

    @Autowired
    private UserLoginService loginService;

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(HttpServletRequest request, HttpServletResponse response,
                              String username, String password){
        TaotaoResult result = loginService.login(username, password);
        // 把token设置到cookie中
        if(result.getStatus() == 200){
            String token = (String) result.getData();
            CookieUtils.setCookie(request, response,TT_TOKEN_KEY, token);
        }
        return result;
    }

    /**
     * 验证token是否存在，存在则返回 user对象
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback){
        //判断callback是否存在，是否是Jsonp请求
        //如果是jsonp请求需要伪装，需要拼接类似 fun({id:""})
        TaotaoResult result = loginService.getUserByToken(token);
        if(StringUtils.isNotBlank(callback)){
            String jsonpstr = callback + "("+ JsonUtils.objectToJson(result) +")";
            return jsonpstr;
        }

        return JsonUtils.objectToJson(result);
    }


}
