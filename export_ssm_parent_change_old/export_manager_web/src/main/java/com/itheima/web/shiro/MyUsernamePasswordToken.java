package com.itheima.web.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class MyUsernamePasswordToken extends UsernamePasswordToken {

    private String isOpenid;

    public String getIsOpenid() {
        return isOpenid;
    }

    public void setIsOpenid(String isOpenid) {
        this.isOpenid = isOpenid;
    }

    public MyUsernamePasswordToken(){

    }

    public MyUsernamePasswordToken(String username, char[] password) {
        super(username, password);
    }

    public MyUsernamePasswordToken(String username, String password) {
        super(username, password);
    }

    public MyUsernamePasswordToken(String username, char[] password, String host) {
        super(username, password, host);
    }

    public MyUsernamePasswordToken(String username, String password, String host) {
        super(username, password, host);
    }

    public MyUsernamePasswordToken(String username, char[] password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public MyUsernamePasswordToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public MyUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public MyUsernamePasswordToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

}
