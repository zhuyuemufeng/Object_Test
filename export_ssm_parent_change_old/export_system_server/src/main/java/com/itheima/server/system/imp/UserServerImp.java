package com.itheima.server.system.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.Encrypt;
import com.itheima.dao.system.UserMapper;
import com.itheima.domain.system.User;
import com.itheima.server.system.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;
import java.util.UUID;

@Service
public class UserServerImp implements UserServer {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private JmsTemplate jmsQueueTemplate;


    public PageInfo<User> findAll(int page, int size, String companyId) {
        PageHelper.startPage(page,size);
        List<User> userList = mapper.findAll(companyId);
        return new PageInfo(userList);
    }

    public User findById(String userId) {
        return mapper.findById(userId);
    }

    public int delete(String userId) {
        return mapper.delete(userId);
    }

    public int save(User user) {
        String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        user.setId(id);
        String password = user.getPassword();
        String md5Psd = Encrypt.md5(password,user.getEmail());
        user.setPassword(md5Psd);
        int save = mapper.save(user);
        jmsQueueTemplate.send("user_email", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("userName",user.getUserName());
                mapMessage.setString("companyName",user.getCompanyName());
                mapMessage.setString("email",user.getEmail());
                mapMessage.setString("password",password);
                return mapMessage;
            }
        });



        return save;
    }

    public int update(User user) {
        return mapper.update(user);
    }

    public void updateUserRoles(String[] rolesId, String userId) {
        mapper.deleteUserRoles(userId);
        for (int i = 0; i < rolesId.length; i++) {
            mapper.saveUserRoles(userId,rolesId[i]);
        }
    }

    public User findByEmail(String email) {
        return mapper.findByEmail(email);
    }
}
