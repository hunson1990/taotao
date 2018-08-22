package com.taotao.search.solr;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;




@Repository
public class SolrClientFactory {

    public static SolrClient getSolrClient(){  //这里仅仅是去掉了static关键字
        //单机版
        //ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-solr.xml");
        //return context.getBean("httpSolrClient", HttpSolrClient.class);


        //集群版
        String zkHost = "192.168.31.135:2181,192.168.31.135:2182,192.168.31.135:2183";
        // 集群版本暂时用这个办法，测试了在spring配置但是一直报报错： 报错内容下一行
        //java.lang.ClassNotFoundException: org.apache.solr.client.solrj.SolrServerException
        CloudSolrClient solrClient = new CloudSolrClient.Builder().withZkHost(zkHost).build();
        //2. 设置默认的搜索collection，默认的索引库（不是core所对应的，是整个collection索引集合）
        solrClient.setDefaultCollection("collection2");
        return solrClient;

    }

}
