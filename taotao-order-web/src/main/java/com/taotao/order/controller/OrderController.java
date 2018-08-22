package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbItem;
import com.taotao.common.pojo.TbUser;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String order_cart(HttpServletRequest request, Model model) throws IOException {
        TbUser user = (TbUser)request.getAttribute("USER_INFO");
        Long userId = user.getId();
        List<TbItem> itemList = cartService.getCartItems(userId);
        model.addAttribute("cartList",itemList);
        return "order-cart";
    }

    /**
     * url : /order/create
     * 参数 : 表单orderInfo接受
     * @param info
     * @return
     */
    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String createOrder(HttpServletRequest request, OrderInfo info){
        //设置orderInfo的uerId
        TbUser user = (TbUser)request.getAttribute("USER_INFO");
        Long userId = user.getId();
        info.setUserId(userId);
        info.setBuyerNick(user.getUsername());
        //调用服务
        TaotaoResult result = orderService.createOrder(info);
        request.setAttribute("orderId", result.getData());
        request.setAttribute("payment", info.getPayment());
        DateTime dateTime = new DateTime();
        DateTime plusDays = dateTime.plusDays(3); //添加三天
        request.setAttribute("date", plusDays.toString("yyyy-MM-dd"));
        return "success";
    }

}
