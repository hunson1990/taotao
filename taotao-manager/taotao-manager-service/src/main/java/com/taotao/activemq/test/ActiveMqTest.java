package com.taotao.activemq.test;



import com.taotao.activemq.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-activemq.xml"})
public class ActiveMqTest {

    @Resource(name = "topicSender")
    private TopicSender topicSender;

    @Test
    public void testSend(){
        topicSender.sendMessage("topic发送的消息2");
    }

}
