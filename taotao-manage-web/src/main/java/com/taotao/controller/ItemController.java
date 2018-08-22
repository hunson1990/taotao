package com.taotao.controller;



import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbItem;
import com.taotao.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    /**
     * url: /item/list
     * method: get
     * 参数: page, rows
     * 返回值: json
     */
    @RequestMapping(value = "/item/list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){
        //1 引入服务

        //2 注入服务
        //3 调用服务并返回
        EasyUIDataGridResult easyUIDataGridResult = itemService.getItemList(page, rows);
        return easyUIDataGridResult;
    }

    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createItem(TbItem item, String desc){
        //1 引入服务

        //2 注入服务
        //3 调用服务并返回

        return itemService.createTbItem(item, desc);
    }

}

