package com.itheima.web.shiro;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.server.system.ModuleServer;
import com.itheima.server.system.UserServer;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;

public class userRealm extends AuthorizingRealm {

    @Reference
    private UserServer server;

    @Reference
    private ModuleServer moduleServer;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User)principalCollection.getPrimaryPrincipal();
        List<Module> userModule = moduleServer.findUserModule(user);
        HashSet<String> hashSet = new HashSet<>();
        for (Module module:userModule){
            hashSet.add(module.getName());
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(hashSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("进入认证程序");
        MyUsernamePasswordToken token = (MyUsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = server.findByEmail(username);
        if (user!=null){
            //1.数据库取出得对象 2.数据库中的密码 3.当前realm名称
            //token将由容器自动传入到比较器呢
            /**
             * 网上搜索的介绍
             * 这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password。
             * 从这里传入的password（这里是从数据库获取的）和token（filter中登录时生成的）中的password
             * 做对比，如果相同就允许登录，不相同就抛出异常。
             */
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
            return info;

        }
        return null;
    }
}
