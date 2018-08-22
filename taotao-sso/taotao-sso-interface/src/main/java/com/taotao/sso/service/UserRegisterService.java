package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbUser;


/**
 * 用户注册接口
 */
public interface UserRegisterService {
    /**
     * 根据参数和类型进行校验
     * @param param 要校验的数据
     * @param type 1，2，3分别代表 username，phone，email
     * @return
     */
    public TaotaoResult checkData(String param, Integer type);

    /**
     * 注册用户
     * @param tbUser
     * @return
     */
    public TaotaoResult register(TbUser tbUser);


}
