package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCateGoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * url: /content/category/list
     * method: "get"
     */
    @RequestMapping(value = "/content/category/list", method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getCategoryList(@RequestParam(value = "id", defaultValue = "0") Long parentId){
        //1 引入服务
        //2 注入服务
        //3 调用服务
        List<EasyUITreeNode> contentCategoryList = contentCategoryService.getContentCategoryList(parentId);
        return contentCategoryList;
    }

    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createContentCategory(Long parentId, String name){
        TaotaoResult contentCategory = contentCategoryService.createContentCategory(parentId, name);
        return contentCategory;
    }

    @RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategory(Long id, String name){
        TaotaoResult contentCategory = contentCategoryService.updateContentCategory(id, name);
        return contentCategory;
    }

    @RequestMapping(value = "/content/category/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long id){
        TaotaoResult contentCategory = contentCategoryService.deldeteContentCategory(id);
        return contentCategory;
    }


}
