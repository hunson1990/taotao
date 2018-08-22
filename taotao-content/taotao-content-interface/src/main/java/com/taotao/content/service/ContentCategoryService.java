package com.taotao.content.service;


import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    /**
     * 通过节点id，查询该节点的子节点列表
     * @param parentId
     * @return
     */
    public List<EasyUITreeNode> getContentCategoryList(long parentId);

    public TaotaoResult createContentCategory(Long parentId, String name);

    public TaotaoResult updateContentCategory(Long id, String name);

    public TaotaoResult deldeteContentCategory(Long id);
}
