package com.taotao.test.jedis;

import com.taotao.content.jedis.JedisClient;
import com.taotao.content.jedis.JedisClientPool;
import javafx.application.Application;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisClient {
    @Test
    public void testDanji(){
        //1 初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //2 获取实现类实例
        JedisClient bean = context.getBean(JedisClient.class);
        //3 调用方法操作
        bean.set("jedisClientKey2","111111");
        System.out.println(bean.get("jedisClientKey2"));
    }
}
