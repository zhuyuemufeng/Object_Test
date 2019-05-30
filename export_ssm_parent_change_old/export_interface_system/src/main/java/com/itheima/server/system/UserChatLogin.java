package com.itheima.server.system;

import com.itheima.domain.system.User;
import com.itheima.domain.system.WechatUser;

public interface UserChatLogin {

    WechatUser Login(String code);

    int registerWechatNo(String code, User user);
}
