package com.taotao.activemq.test;



import com.taotao.activemq.spring.SpringTopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.Topic;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContent-activemq.xml"})
public class ActiveMqTest {

    @Resource(name = "topicSender")
    private SpringTopicSender topicSender;

    @Test
    public void testSend(){
        topicSender.sendMessage("topic发送的消息");
    }

}
