package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbOrder;
import com.taotao.common.pojo.TbOrderItem;
import com.taotao.common.pojo.TbOrderShipping;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //1 插入订单
            //生成的订单ID
        String orderId = IDUtils.getOrderId();
            //补全其他属性
        orderInfo.setOrderId(orderId);
        orderInfo.setCreateTime(new Date());
        orderInfo.setPostFee("0"); //邮费
        orderInfo.setStatus(1); //订单状态
//        orderInfo.setUserId(); // 由controller设置
        orderInfo.setUpdateTime(orderInfo.getCreateTime());
            //注入mapper插入
        orderMapper.insert(orderInfo);
        //2 插入订单详情
            //设置订单详情的id
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for(TbOrderItem tbOrderItem : orderItems){
            //补全其他属性
            long itemId = IDUtils.genItemId();
            tbOrderItem.setId(String.valueOf(itemId));
            tbOrderItem.setOrderId(orderId);
            //插入订单详情
            orderItemMapper.insert(tbOrderItem);
        }

        //3 插入订单物流
            //设置订单id，为物流的id
        TbOrderShipping orderShipping = new TbOrderShipping();
        orderShipping.setOrderId(orderId);
            //补全其他属性
        orderShipping.setCreated(orderInfo.getCreateTime());
        orderShipping.setUpdated(orderInfo.getUpdateTime());
            //插入物流表
        orderShippingMapper.insert(orderShipping);
        //4 返回包含orderId的result
        return TaotaoResult.ok(orderId);
    }

}
