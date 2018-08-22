package com.taotao.sso.controller;


import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    /**
     * 校验数据
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type){
        return userRegisterService.checkData(param,type);
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult registe(TbUser user){
        return userRegisterService.register(user);
    }

}
