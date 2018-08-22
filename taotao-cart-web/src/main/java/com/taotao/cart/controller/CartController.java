package com.taotao.cart.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbItem;
import com.taotao.common.pojo.TbUser;
import com.taotao.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    private static Long getRequestUserId(HttpServletRequest request){
        TbUser user = (TbUser)request.getAttribute("USER_INFO");
        Long userId = user.getId();
        return userId;
    }

    @RequestMapping("/cart/cart")
    public String cart(Model model,HttpServletRequest request) throws IOException {
        Long userId = CartController.getRequestUserId(request);
        List<TbItem> itemList = cartService.getCartItems(userId);
        model.addAttribute("cartList",itemList);
        return "cart";
    }


    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, HttpServletRequest request) throws IOException {

        Long userId = CartController.getRequestUserId(request);
        //添加购物车商品，默认数量为1
        cartService.addCartItem(itemId,1, userId);
        return "cartSuccess";
    }


    @RequestMapping("/cart/update/num/{itemId}/{itemNum}")
    @ResponseBody
    public String updateCart(@PathVariable Long itemId, @PathVariable Integer itemNum, HttpServletRequest request) throws IOException {
        Long userId = CartController.getRequestUserId(request);
        TaotaoResult result = cartService.updateCartItem(itemId, itemNum, userId);
        return JsonUtils.objectToJson(result);
    }


    @RequestMapping("/cart/delete/{itemId}")
    public String delCart(@PathVariable Long itemId, HttpServletRequest request) throws IOException {
        Long userId = CartController.getRequestUserId(request);
        cartService.delCartItem(itemId, userId);
        return "redirect:/cart/cart.html";
    }

}
