package com.itheima.jms.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 监听消息队列中的消息，需要实现默认接口MessageListener
 */

@Component
public class EmailLister implements MessageListener {

    @Autowired
    private JavaMailSenderImpl messageSender;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Override
    public void onMessage(Message message) {
        try{
            MapMessage mapMessage = (MapMessage)message;
            String userName = mapMessage.getString("userName");
            String companyName = mapMessage.getString("companyName");
            String email = mapMessage.getString("email");
            String password = mapMessage.getString("password");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("入职邮件");
            String content = userName+"先生/女士，您好，欢迎您加入"+companyName+"，在这里我们将一起共同努力，您的平台账户已申请，" +
                    "登录账户："+email+"，密码："+password+"，请尽快更改密码。";
            simpleMailMessage.setText(content);
            messageSender.send(simpleMailMessage);
        }catch (Exception e){

        }
    }
}
