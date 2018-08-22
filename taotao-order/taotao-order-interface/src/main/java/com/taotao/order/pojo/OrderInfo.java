package com.taotao.order.pojo;

import com.taotao.common.pojo.TbOrder;
import com.taotao.common.pojo.TbOrderItem;
import com.taotao.common.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

public class OrderInfo extends TbOrder implements Serializable {

    private List<TbOrderItem> orderItems; //订单详细

    private TbOrderShipping tbOrderShipping; //订单收货地址

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getTbOrderShipping() {
        return tbOrderShipping;
    }

    public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
        this.tbOrderShipping = tbOrderShipping;
    }
}
