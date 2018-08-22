package com.taotao.cart.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbItem;

import java.io.IOException;
import java.util.List;

public interface CartService {

    /**
     * 登陆状态下：
     * 根据：token获取购物车的商品，（redis中，保存时间为7天）
     * token获取到user，根据userId再获取对应id的商品;
     * @param userId
     * @return
     */
    public List<TbItem> getCartItems(Long userId) throws IOException;

    /**
     * 添加商品到购物车，(一次只能添加一个商品，商品数量默认为1）
     * @param itemId
     * @param itemNum
     * @return
     */
    public TaotaoResult addCartItem(Long itemId, Integer itemNum, Long userId) throws IOException;


    /**
     * 修改购物车里商品数量
     * @return
     */
    public TaotaoResult updateCartItem(Long itemId, Integer itemNum, Long userId) throws IOException;


    /**
     * 删除购物车商品
     * @param itemId
     * @param userId
     * @return
     */
    public TaotaoResult delCartItem(Long itemId, Long userId) throws IOException;

}
