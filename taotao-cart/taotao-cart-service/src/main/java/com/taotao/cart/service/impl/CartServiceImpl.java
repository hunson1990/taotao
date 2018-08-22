package com.taotao.cart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.jedis.JedisClient;
import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbItem;
import com.taotao.common.pojo.TbItemExample;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient client;
    @Value("${CART_INFO}")
    private String CART_INFO;
    @Autowired
    private TbItemMapper itemMapper;

    private ObjectMapper mapper = new ObjectMapper();//jackson处理json和list转化

    public static Integer itemExsits(Long itemId, List<TbItem> list){
        //判断list里面是否存在某个item
        //如果存在就返回item在list里面的位置，否则返回null
        for(int i = 0 ; i < list.size() ; i++) {
            TbItem tbItem = list.get(i);
            if(itemId.equals(tbItem.getId())){
                //如果itemId == list里面某id， 说明存在item
                return i;
            }
        }
        return null;
    }


    @Override
    public List<TbItem> getCartItems(Long userId) throws IOException {
        String itemsJson = client.get(CART_INFO + ":" + userId);
        List<TbItem> itemList = mapper.readValue(itemsJson, new TypeReference<List<TbItem>>() {});

        return itemList;
    }


    @Override
    public TaotaoResult updateCartItem(Long itemId, Integer itemNum, Long userId) throws IOException {
        String itemsJson = client.get(CART_INFO + ":" + userId);
        List<TbItem> itemList = mapper.readValue(itemsJson, new TypeReference<List<TbItem>>() {});
        Integer integer = CartServiceImpl.itemExsits(itemId, itemList);
        if (integer == null){
            return TaotaoResult.build(400,"此商品不存在");
        }
        itemList.get(integer).setNum(itemNum);
        //转化成json添加到redis
        String s = mapper.writeValueAsString(itemList);
        client.set(CART_INFO+":"+userId, s);
        return TaotaoResult.ok();
    }


    @Override
    public TaotaoResult addCartItem(Long itemId, Integer itemNum, Long userId) throws IOException {
        TbItemExample itemExample = new TbItemExample();
        TbItemExample.Criteria criteria = itemExample.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> tbItems = itemMapper.selectByExample(itemExample);
        if (tbItems.size() !=1 ){
            return TaotaoResult.build(400,"商品有误");
        }
        TbItem item = tbItems.get(0);
        //先判断有没有购物车
        Boolean exists = client.exists(CART_INFO + ":" + userId);
        //如果没有购物车，就新建list，把商品放进去，转化为json存到redis
        if(!exists){
            //没有购物车
            item.setNum(1); //给商品数量初始化为1
            List<TbItem> list = new ArrayList<>();
            list.add(item);
            String itemsJson = mapper.writeValueAsString(list);
            client.set(CART_INFO+":"+userId, itemsJson);
            return TaotaoResult.ok();
        }
        //如果有购物车，就取出商品json信息转化成list，再把商品放进去，存到redis
        String itemsJson = client.get(CART_INFO + ":" + userId);
        List<TbItem> itemList = mapper.readValue(itemsJson, new TypeReference<List<TbItem>>() {});
        //先判断里面有没有这个商品，有则数量+1 执行修改，没有则直接添加
        Integer integer = CartServiceImpl.itemExsits(itemId, itemList);
        if(integer != null){
            Integer num = itemList.get(integer).getNum();
            itemList.get(integer).setNum(num+1);
        }else{
            //把商品添加到list里面
            item.setNum(1); //给商品数量初始化为1
            itemList.add(item);
        }
        //转化成json添加到redis
        String s = mapper.writeValueAsString(itemList);
        client.set(CART_INFO+":"+userId, s);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult delCartItem(Long itemId, Long userId) throws IOException {
        //判断商品是否存在
        String cartKey = CART_INFO + ":" + userId;
        String itemsJson = client.get(cartKey);
        List<TbItem> itemList = mapper.readValue(itemsJson, new TypeReference<List<TbItem>>() {});
        Integer integer = CartServiceImpl.itemExsits(itemId, itemList);
        if(integer == null){
            //从list里面把该商品去除
            return TaotaoResult.build(400,"此商品不存在.");
        }
        itemList.remove(itemList.get(integer));
        System.out.println(itemList);
        String s = mapper.writeValueAsString(itemList);
        System.out.println(s);
        client.set(CART_INFO+":"+userId, s);
        return TaotaoResult.ok();
    }




    @Test
    public void test(){
        TbItem item = new TbItem();
        item.setTitle("测试item---1");
        TbItem item2 = new TbItem();
        item2.setTitle("测试item---2");

        List<TbItem> list = new ArrayList<>();
        list.add(item);
        list.add(item2);
        list.get(0).setTitle("修改后");

        for(TbItem tbitem : list){
            System.out.println(tbitem.getTitle());
        }



        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonlist = mapper.writeValueAsString(list);
            System.out.println(jsonlist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
