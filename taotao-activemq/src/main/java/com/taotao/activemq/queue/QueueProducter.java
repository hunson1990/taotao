package com.taotao.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;


public class QueueProducter {
    //生产者发送消息

    @Test
    public void send() throws Exception{
        //1 创建一个连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.31.134:61616");
        //2 通过工厂获取连接对象
        Connection connection = factory.createConnection();
        //3 开启连接
        connection.start();
        //4 创建一个session对象，提供发送消息等方法
        //第一个参数：表示是否开启分布式事物
        //第二个参数：就是设置消息的应答模式，如果第一个参数是false，第二个参数设置才有意义, 自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5 创建目的地（destination）queue
        // 参数是目的地名称
        Queue queue = session.createQueue("queue-test");
        //6 创建个生产者
        MessageProducer producer = session.createProducer(queue);
        //7 构建消息内容
        TextMessage textMessage = session.createTextMessage("queue发送的消息2");
        //8 发送消息
        producer.send(textMessage);
        //9 关闭资源
        producer.close();
        session.close();
        connection.close();
    }


}
