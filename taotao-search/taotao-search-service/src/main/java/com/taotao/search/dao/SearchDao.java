package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.solr.SolrClientFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class SearchDao {

    @Autowired
    private SearchItemMapper searchItemMapper;

    /**
     * 根据查询条件，查询结果集
     * @param solrQuery
     * @return
     */
    public SearchResult search(SolrQuery solrQuery) throws Exception{

        SearchResult searchResult = new SearchResult();
        //1 获取一个solrclient(由getSolrClient控制集群还是单机版)
        SolrClient solrClient = SolrClientFactory.getSolrClient();
        //2 执行查询
        QueryResponse response = solrClient.query(solrQuery);
        //3 获取结果集
        SolrDocumentList results = response.getResults();
        //4 遍历结果集合
        List<SearchItem> searchItemList = new ArrayList<>();
        //取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for(SolrDocument document : results){
            // 将doc里面的属性值，一个个的转化成searchItem
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name(document.get("item_category_name").toString());
            searchItem.setId(Long.parseLong(document.get("id").toString()));
            searchItem.setImage(document.get("item_image").toString());
            //searchItem.setItem_desc();
            searchItem.setPrice((Long) document.get("item_price"));
            searchItem.setSell_point(document.get("item_sell_point").toString());
            //取高亮
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            //判断list是否为空
            String title_highlight = "";
            if(list != null && list.size()>0){
                //说明有高亮
                title_highlight = list.get(0);
            }else{
                title_highlight = document.get("item_title").toString();
            }
            searchItem.setTitle(title_highlight);
            // 将searchItem 封装到 searchResult 里面
            searchItemList.add(searchItem);
        }
        //5 包装searchResult
        searchResult.setRecordCount(results.getNumFound());
        searchResult.setItemList(searchItemList);

        return searchResult;
    }

    /**
     * 更新索引库
     * @param itemId
     * @return
     * @throws Exception
     */
    public TaotaoResult updateSearchItemById(Long itemId) throws Exception{
        //注入mapper

        //查询到记录
        SearchItem searchItem = searchItemMapper.getSearchItemById(itemId);
        //把记录更新到索引库
            //创建一个solrServer 注入进来
        SolrClient solrClient = SolrClientFactory.getSolrClient();
            //创建solrinputdocument 对象
        SolrInputDocument document = new SolrInputDocument();
            //向文档对象添加域
        System.out.println(searchItem);
        document.addField("id", searchItem.getId().toString());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("item_desc", searchItem.getItem_desc());
            //向索引库中添加文档
        solrClient.add(document);
            //提交
        solrClient.commit();

        return TaotaoResult.ok();
    }



}
