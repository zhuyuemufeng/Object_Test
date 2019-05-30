package com.itheima;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producter {

    public void textProduce(String messages) throws Exception{
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer messageProducer = null;
        Message message = null;

        connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("frist_queue");
        messageProducer = session.createProducer(destination);
        for (int i = 0; i < 10; i++) {
            message = session.createTextMessage(i+"");
            messageProducer.send(message);
        }
        System.out.println("消息已发送");
        connection.close();
        session.close();
        messageProducer.close();


    }

    public static void main(String[] args) throws Exception {
        Producter producter = new Producter();
        producter.textProduce("这是第一条信息");
    }
}
