package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;


public interface SearchService {
    //导入所有的商品数据 到索引库
    public TaotaoResult importAllSearchItems();

    //根据搜索条件，查询结果

    /**
     *
     * @param querystr 查询的主条件
     * @param page 查询当前页码
     * @param rows 每页显示的行数， 在controller中写死就可以了
     * @return
     * @throws Exception
     */
    public SearchResult search(String querystr, Integer page, Integer rows) throws Exception;
}
