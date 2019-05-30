package com.itheima;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AutoCusumer {

    public void getMessage() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createQueue("frist_queue");
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    message.acknowledge();
                    TextMessage textMessage = (TextMessage)message;
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws JMSException {
        AutoCusumer autoCusumer = new AutoCusumer();
        autoCusumer.getMessage();
    }
}
