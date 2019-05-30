package com.itheima.web.shiro;

import com.itheima.common.utils.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class userMathcer extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //AuthenticationToken 容器自动封装，首先必须进行转型取出前台得数据
        MyUsernamePasswordToken token1 = (MyUsernamePasswordToken)token;
        SimpleAuthenticationInfo info1 = (SimpleAuthenticationInfo)info;
        String uPassword = new String(token1.getPassword());
        String username = new String(token1.getUsername());
        String str = null;
        String dbPassword = (String) info.getCredentials();
        if(token1.getIsOpenid()==null){
            //那host中存储的openId去匹配
            str = Encrypt.md5(uPassword, token1.getUsername());
        }else {
            str = uPassword;
        }
        return str.equals(dbPassword);
    }
}
