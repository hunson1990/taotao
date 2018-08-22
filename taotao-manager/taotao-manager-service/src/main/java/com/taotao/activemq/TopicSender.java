package com.taotao.activemq;


import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


@Service
public class TopicSender {

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
