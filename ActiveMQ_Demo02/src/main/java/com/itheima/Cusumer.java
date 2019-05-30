package com.itheima;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Cusumer {

    public String getMessage() throws Exception{

        final String brokeUrl = "tcp://127.0.0.1:61616";
        //ActiveMQ的连接工厂
        ConnectionFactory connectionFactory = null;
        //操作ActiveMQ的连接
        Connection connection = null;
        //与ActiveMQ的一次会话
        Session session = null;
        //目的地
        Destination destination = null;
        //消息消费者
        MessageConsumer consumer = null;
        //消息的顶级接口
        Message message = null;

        connectionFactory = new ActiveMQConnectionFactory(brokeUrl);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("frist_queue");
        consumer =  session.createConsumer(destination);
        message = consumer.receive();
        TextMessage tx = (TextMessage)message;
        String text = tx.getText();
        return text;
    }

    public static void main(String[] args) throws Exception {
        Cusumer cusumer = new Cusumer();
        String message = cusumer.getMessage();
        System.out.println(message);
    }
}
