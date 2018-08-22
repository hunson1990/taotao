package com.taotao.search.controller;


import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {

    //每页显示的条数
    private static Integer ROWS = 60;

    @Autowired
    private SearchService searchService;

    /**
     * 根据条件搜索商品数据
     * @param page
     * @param querystr
     * @return
     */
    @RequestMapping("/search")
    public String search(Integer page, Model model, @RequestParam(value = "q") String querystr){

        // 处理 get请求，中文乱码
        try {
            querystr = new String(querystr.getBytes("iso-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用service服务
        SearchResult result = null;
        System.out.println(querystr);
        try {
            result = searchService.search(querystr, page, ROWS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置数据传递到jsp
        model.addAttribute("query", querystr);
        model.addAttribute("totalPages", result.getPageCount());
        model.addAttribute("itemList",result.getItemList());
        model.addAttribute("page",page);
        return "search";

        //./zkcli.sh -zkhost 192.168.31.135:2181,192.168.31.135:2182,192.168.31.135:2183 -cmd upconfig -confdir /usr/local/solr-cloud/solrhome01/collection1/conf -confname myconf
    }
}
