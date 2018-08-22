package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbContent;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * url: /content/save
     * method: "get"
     * 插入数据
     */
    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult insertContent(TbContent tbContent){
        contentService.insertContent(tbContent);
        return TaotaoResult.ok();
    }

    /**
     * 查询显示数据
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/content/query/list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult showContent(Long categoryId, Integer page, Integer rows){
        return contentService.getContent(categoryId, page, rows);
    }

    /**
     * 删除一个或多个内容
     * @param ids
     * @return
     */
    @RequestMapping(value = "/content/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContent(String ids){
        String[] idList = ids.split(",");
        List<Long> idList_lon = new ArrayList<Long>();
        for(String id : idList){
            System.out.println(id);
            idList_lon.add(Long.valueOf(id));
        }
        System.out.println(idList_lon);
        return contentService.deleteContent(idList_lon);
    }

    @RequestMapping(value = "/rest/content/edit", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult update(TbContent content){
        return contentService.updateContent(content);
    }
}
