package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;
import com.taotao.search.solr.SolrClientFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class SearchServiceImpl implements SearchService {


    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String querystr, Integer page, Integer rows) throws Exception {
//        SearchResult searchResult = new SearchResult();
//        searchResult.setPageCount(20);
//        searchResult.setRecordCount(1000);
//        searchResult.setItemList(null);
        //1 创建一个solrquery对象
        SolrQuery solrQuery = new SolrQuery();

        //2 设置主查询条件
        if(querystr != null && !querystr.equals("")){
            solrQuery.setQuery(querystr);
        }else{
            solrQuery.setQuery("*:*");
        }
        //2.1 设置过滤条件,分页
        if(page==null)page=1;
        if(rows==null)rows=60;
        solrQuery.setStart((page-1)*rows);// (page-1)*rows
        solrQuery.setRows(rows);
        //2.2 设置默认的搜索域
        solrQuery.set("df","item_keywords");
        //2.3 设置高亮
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");
        solrQuery.addHighlightField("item_title"); //设置高亮显示的域
        System.out.println(solrQuery);

        //3 调用dao的查询方法， 返回（SearchResult）， 这里包含总记录数，商品列表
        SearchResult searchResult = searchDao.search(solrQuery);
        //4 设置searchresult的总页数
        long pageCount =(long) Math.ceil((double)searchResult.getRecordCount()/rows);
        searchResult.setPageCount(pageCount);
        return searchResult;
    }


    @Override
    public TaotaoResult importAllSearchItems() {
        List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();
        // 通过solrj 将这些数据写入到索引库
        //1 创建solrserver 建立连接 需要指定地址
        System.out.println(searchItemList);
        SolrClient solrClient = SolrClientFactory.getSolrClient();
        // 将列表中的数据一个个的放到索引中去
        for(SearchItem searchItem : searchItemList) {
            //2 创建solrinputdocument
            SolrInputDocument document = new SolrInputDocument();
            //3 向文档中添加 域
            document.addField("id", searchItem.getId().toString());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            //4 将文档提交到索引库中

            try {
                solrClient.add(document);
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //5 提交
        try {
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return TaotaoResult.ok();
    }



}
