package com.taotao.portal.controller;

import com.taotao.common.pojo.TbContent;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.portal.pojo.Ad1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ContentService contentService;


    @Value("${AD1_CATEGORY_ID}")
    private Long categoryId;
    @Value("${AD1_HEIGHT}")
    private String AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private String AD1_HEIGHT_B;
    @Value("${AD1_WIDTH}")
    private String AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private String AD1_WIDTH_B;

    @RequestMapping("/index")
    public String showIndex(Model model){
        //1 根据内容分类id，查询内容列表
        System.out.println(categoryId);
        List<TbContent> contentList = contentService.getContentListByCatId(categoryId);
        //2 转化成自己的pojo列表
        System.out.println(contentList);
        List<Ad1Node> nodes = new ArrayList<>();
        for(TbContent tbContent : contentList){
            Ad1Node ad1Node = new Ad1Node();
            ad1Node.setAlt(tbContent.getSubTitle());
            ad1Node.setHeight(AD1_HEIGHT);
            ad1Node.setHeightB(AD1_HEIGHT_B);
            ad1Node.setHref(tbContent.getUrl());
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setSrcB(tbContent.getPic2());
            ad1Node.setWidth(AD1_WIDTH);
            ad1Node.setWidthB(AD1_WIDTH_B);
            nodes.add(ad1Node);
        }

        //3 传递数据给jsp
        model.addAttribute("ad1",JsonUtils.objectToJson(nodes));
        System.out.println(JsonUtils.objectToJson(nodes));
        return "index";
    }
}
