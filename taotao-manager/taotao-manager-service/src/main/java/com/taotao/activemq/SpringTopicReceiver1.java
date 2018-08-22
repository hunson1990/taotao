package com.taotao.activemq;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@Service
public class SpringTopicReceiver1 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        //获取消息
        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try{
                System.out.println("监听的消息：" + textMessage.getText());
            }catch (JMSException e){
                e.printStackTrace();
            }

        }
    }

}
