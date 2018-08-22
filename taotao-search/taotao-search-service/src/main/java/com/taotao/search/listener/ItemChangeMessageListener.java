package com.taotao.search.listener;

import com.taotao.search.dao.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@Service
public class ItemChangeMessageListener implements MessageListener {

    @Autowired
    SearchDao searchDao;

    @Override
    public void onMessage(Message message) {
        //获取消息
        System.out.println("监听message");
        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try{
                //System.out.println(textMessage.getText());
                Long itemId = Long.valueOf(textMessage.getText());
                System.out.println("search监听到的id ： "+itemId);
                searchDao.updateSearchItemById(itemId);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
