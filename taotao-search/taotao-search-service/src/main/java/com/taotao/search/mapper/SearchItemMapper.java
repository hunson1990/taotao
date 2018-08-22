package com.taotao.search.mapper;


import com.taotao.common.pojo.SearchItem;

import java.util.List;

/**
 * 定义Mapper关联词查询三张表，查询出搜索池的数据
 */
public interface SearchItemMapper {
    //查询所有商品的数据
    public List<SearchItem> getSearchItemList();

    //根据商品id查询商品数据
    public  SearchItem getSearchItemById(Long itemId);
}
