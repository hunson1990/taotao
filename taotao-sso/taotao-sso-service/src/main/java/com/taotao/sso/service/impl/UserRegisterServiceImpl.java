package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbUser;
import com.taotao.common.pojo.TbUserExample;
import com.taotao.mapper.TbUserMapper;
import com.taotao.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public TaotaoResult register(TbUser tbUser) {
        //1 校验数据
        if(StringUtils.isEmpty(tbUser.getUsername())){
            return TaotaoResult.build(400,"注册失败，请校验数据后再提交数据");
        }
        if(StringUtils.isEmpty(tbUser.getPassword())){
            return TaotaoResult.build(400,"注册失败，请校验数据后再提交数据");
        }
        // 校验username，phome，email是否被注册
        TaotaoResult taotaoResult = checkData(tbUser.getUsername(), 1);
        if(!(Boolean) taotaoResult.getData()){
            return TaotaoResult.build(400,"注册失败，该用户已被注册");
        }
        if(StringUtils.isNotBlank(tbUser.getPhone())){
            TaotaoResult taotaoResult2 = checkData(tbUser.getUsername(), 2);
            if(!(Boolean)taotaoResult2.getData()){
                return TaotaoResult.build(400,"注册失败，该手机已被注册");
            }
        }
        if(StringUtils.isNotBlank(tbUser.getEmail())){
            TaotaoResult taotaoResult3 = checkData(tbUser.getUsername(), 3);
            if(!(Boolean)taotaoResult3.getData()){
                return TaotaoResult.build(400,"注册失败，该邮箱已被注册");
            }
        }
        //2 校验成功，则补全数据，插入数据
        tbUser.setCreated(new Date());
        tbUser.setUpdated(tbUser.getCreated());
        //3 对密码进行加密
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        //4 插入数据
        tbUserMapper.insert(tbUser);
        return TaotaoResult.ok();
    }


    @Override
    public TaotaoResult checkData(String param, Integer type) {
        //1 注入mapper
        //2 根据param和type动态生成查询条件
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        if(type == 1){//username
            if(StringUtils.isEmpty(param)){
                return TaotaoResult.ok(false);
            }
            criteria.andUsernameEqualTo(param);
        }else if(type == 2){// phone
            criteria.andPhoneEqualTo(param);
        }else if(type == 3){//email
            criteria.andEmailEqualTo(param);
        }else {
            return TaotaoResult.build(400,"非法参数");
        }
        //3 获取mapper的查询方法，获取数据
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        if(tbUsers !=null && tbUsers.size() >0){
            return TaotaoResult.ok(false);
        }
        //4 如果查询到了数据，数据不可用，false
        //5 如果没有查到数据，说明可以用，true
        return TaotaoResult.ok(true);
    }

}
