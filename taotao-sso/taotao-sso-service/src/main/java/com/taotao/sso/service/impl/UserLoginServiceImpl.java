package com.taotao.sso.service.impl;


import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbUser;
import com.taotao.common.pojo.TbUserExample;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;


@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient client;

    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${EXPIRE_TIME}")
    private Integer EXPIRE_TIME;
    @Value("${SSO_BASE_URL}")
    public String SSO_BASE_URL;
    @Value("${SSO_PAGR_LOGIN}")
    public String SSO_PAGR_LOGIN;




    @Override
    public TaotaoResult getUserByToken(String token) {
        String userJson = client.get(USER_INFO+":" + token);
        if (StringUtils.isNotBlank(userJson)){
            TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);
            //重置过期时间
            client.expire(USER_INFO + ":" + token, EXPIRE_TIME);
            return TaotaoResult.ok(user);
        }
        return TaotaoResult.build(400,"登陆已过期");
    }

    @Override
    public TaotaoResult logout(String token) {
        //client.hdel()
        return null;
    }

    @Override
    public TaotaoResult login(String username, String password) {
        //1 注入mapper
        //2 校验是否为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return TaotaoResult.build(400,"用户名或密码为空");
        }
        //3 校验数据合法性
            //校验用户名
        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = userMapper.selectByExample(userExample);
        if(tbUsers == null || tbUsers.size() != 1){
            return TaotaoResult.build(400,"用户名或密码有误");
        }
            //校验密码
        TbUser user = tbUsers.get(0);
        String s = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(s)){
            return TaotaoResult.build(400,"用户名或密码有误");
        }
        //4 如果校验成功
        //5 生成token ：uuid生成， 还需要设置token的有效期模拟session 用户数据存到redis（key:token,value:用户数据）
        user.setPassword(null);
        String token = UUID.randomUUID().toString();
        client.set(USER_INFO + ":" + token, JsonUtils.objectToJson(user));
        //设置过期时间
        client.expire(USER_INFO + ":" + token, EXPIRE_TIME);
        return TaotaoResult.ok(token);
    }
}
