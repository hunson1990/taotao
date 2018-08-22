package com.taotao.manager.service;

import com.taotao.common.pojo.EUTreeNode;

import java.util.List;

public interface ItemCatService {
    public List<EUTreeNode> getEUTreeNodes(Long id);
}
