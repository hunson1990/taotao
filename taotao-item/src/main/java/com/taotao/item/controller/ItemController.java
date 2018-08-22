package com.taotao.item.controller;


import com.taotao.common.pojo.TbItem;
import com.taotao.common.pojo.TbItemDesc;
import com.taotao.item.pojo.Item;
import com.taotao.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model){
        //1 引入服务
        //2 注入服务
        //3 调用服务
            //商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);
        //商品描述信息
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        //4 构建item 传给jsp
        Item item = new Item(tbItem);
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);

        return "item";
    }
}
