package com.taotao.test.jedis;


import org.junit.Test;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class JedisTest {
    //测试单机版
    @Test
    public void testJedis(){
        //1. 创建一个jedis对象
        Jedis jedis = new Jedis("192.168.31.101",6379);
        //2. 直接进行操作
        jedis.set("jedis","hello");
        System.out.println(jedis.get("jedis"));
        //3. 关闭jedis
        jedis.close();
    }

    //单机版连接池
    @Test
    public void testJedisPool(){
        //1 创建连接池， 指定端口和地址
        JedisPool jedisPool = new JedisPool("192.168.31.101",6379);
        //2 获取jedis对象。
        Jedis jedis = jedisPool.getResource();
        //3 操作jedis
        jedis.set("jedispool","jedispool");
        System.out.println(jedis.get("jedispool"));
        //4. 关闭jedis
        jedis.close();
    }

    //测试集群版
    @Test
    public void testJedisCluster() throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        config =new JedisPoolConfig();
        config.setMaxTotal(60000);//设置最大连接数
        config.setMaxIdle(1000); //设置最大空闲数
        config.setMaxWaitMillis(3000);//设置超时时间
        config.setTestOnBorrow(true);

        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.31.101",7001));
        nodes.add(new HostAndPort("192.168.31.101",7002));
        nodes.add(new HostAndPort("192.168.31.101",7003));
        nodes.add(new HostAndPort("192.168.31.101",7004));
        nodes.add(new HostAndPort("192.168.31.101",7005));
        nodes.add(new HostAndPort("192.168.31.101",7006));
        //1 创建jediscluster 对象
        JedisCluster jedisCluster = new JedisCluster(nodes, config);
        //2 jediscluster 对象操作redis集群
        jedisCluster.set("keyCluster","keyCluster");
        System.out.println(jedisCluster.get("keyCluster"));
        //3 关闭对象
        jedisCluster.close();
    }


}
