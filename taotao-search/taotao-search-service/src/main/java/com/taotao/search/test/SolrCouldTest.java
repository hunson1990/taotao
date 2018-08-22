package com.taotao.search.test;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.SolrZkClient;
import org.junit.Test;

import java.io.IOException;

public class SolrCouldTest {

    @Test
    public void testAdd(){
        //1. 创建solrserver，集群版的实现
        String zkHost = "192.168.31.135:2181,192.168.31.135:2182,192.168.31.135:2183";
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHost).build();
        //2. 设置默认的搜索collection，默认的索引库（不是core所对应的，是整个collection索引集合）
        cloudSolrClient.setDefaultCollection("collection2");
        //3. 创建solrinputdocument对象
        SolrInputDocument document = new SolrInputDocument();
        //4 添加域到文档
        document.addField("id","testid");
        document.addField("item_title","今天鸟语花香");
        //5 将文档提交到索引库
        try {
            cloudSolrClient.add(document);
            //6 提交
            cloudSolrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
