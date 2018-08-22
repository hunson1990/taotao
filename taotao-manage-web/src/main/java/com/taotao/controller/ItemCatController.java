package com.taotao.controller;


import com.taotao.common.pojo.EUTreeNode;
import com.taotao.manager.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public List<EUTreeNode> getEUTreeNodes(Long id){
        return itemCatService.getEUTreeNodes(id);
    }

}
