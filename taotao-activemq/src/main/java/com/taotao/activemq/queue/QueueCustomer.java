package com.taotao.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class QueueCustomer {

    @Test
    public void recieve() throws Exception{
        //1 创建一个连接的工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.31.134:61616");
        //2 通过工厂创建连接
        Connection connection = factory.createConnection();
        //3 开启连接
        connection.start();
        //4 创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5 创建接受消息的一个地址
        Queue queue = session.createQueue("queue-test");
        //6 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7 接受消息并打印
        //第一种: 弊端一直阻塞
//        while(true){
//            Message message = consumer.receive(1000000); //设置接受消息的超时时间
//            if(message == null){
//                //如果消息不存在 跳出循环
//                break;
//            }
//            if(message instanceof TextMessage){
//                TextMessage message1 = (TextMessage) message;
//                System.out.println(message1.getText());
//            }
//        }
        //第二种，设置一个监听器
        //这里其实是开启了一个新的线程
        consumer.setMessageListener(new MessageListener() {

            //当有消息的时候会执行下面的逻辑
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage){
                    TextMessage message1 = (TextMessage) message;
                    try {
                        System.out.println(message1.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread.sleep(199999);
        //8 关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
