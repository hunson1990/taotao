package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbContent;

import java.util.List;

public interface ContentService {
    /**
     * 插入内容表一个记录
     * @param tbContent
     * @return
     */
    public TaotaoResult insertContent(TbContent tbContent);

    /**
     * 获取网站分页内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getContent(Long categoryId, Integer page, Integer rows);

    /**
     * 根据内容分类的 Id 查询Content
     * @param categoryId
     * @return
     */
    public List<TbContent> getContentListByCatId(Long categoryId);


    /**
     * 修改content
     * @param content
     * @return
     */
    public TaotaoResult updateContent(TbContent content);

    /**
     * 删除对应contentId的内容
     * @param ids
     * @return
     */

    public TaotaoResult deleteContent(List<Long> ids);
}
