package com.taotao.activemq.spring;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Destination;


@Service
public class SpringTopicSender {

    @Resource(name = "jmsTopicTemplate")
    private JmsTemplate jmsTopicTemplate;

    @Resource(name = "topicDestination")
    private Destination destination;

    //发送消息
    public void sendMessage(final String message) {

        System.out.println("TopicSender发送消息："+message);
        jmsTopicTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // TODO Auto-generated method stub
                return session.createTextMessage(message);
            }
        });
    }


}
