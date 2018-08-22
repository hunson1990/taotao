package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImportAllItemController {

    @Autowired
    private SearchService searchService;

    /**
     * 导入所有的商品数据到 索引库中
     * @return
     */
    @RequestMapping("/index/importAll")
    @ResponseBody
    public TaotaoResult importAll(){
        return searchService.importAllSearchItems();
    }
}
