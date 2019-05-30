package com.itheima.server.system.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.common.utils.HttpUtils;
import com.itheima.dao.system.WechatUserDao;
import com.itheima.domain.system.User;
import com.itheima.domain.system.WechatUser;
import com.itheima.domain.system.WechatUserExample;
import com.itheima.server.system.UserChatLogin;
import com.itheima.staticClassFile.WeChatFiles;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.itheima.staticClassFile.WeChatFiles.wxInfoUrl;

@Service
public class UserChatLoginImpl implements UserChatLogin {

    @Autowired
    private WechatUserDao userDao;

    @Override
    public WechatUser Login(String code) {

        WechatUser user = null;
        //1.根据code获取access_token和openId
        String atUtl = WeChatFiles.accessTokenUrl + "?code="+code+"&appid="+ WeChatFiles.appid+"&secret="+ WeChatFiles.secret+"&grant_type=authorization_code";
        Map<String, Object> map1 = HttpUtils.sendGet(atUtl);
        Object access_token = map1.get("access_token");
        Object openid = map1.get("openid").toString();
        if(access_token == null && openid == null) {
            return user;
        }

        //2.根据openId判断用户是否存在
        WechatUserExample example = new WechatUserExample();
        example.createCriteria().andOpenidEqualTo(openid.toString());
        List<WechatUser> wechatUsers = userDao.selectByExample(example);
        if (wechatUsers.size()!=0) {
            user = wechatUsers.get(0);
        }
        if(user != null) {
            //3.如果用户存在返回用户信息
            return user;
        }else{
            WechatUser wechatUser = new WechatUser();
            wechatUser.setOpenid(openid.toString());
            String wxurl = wxInfoUrl + "?access_token=" + access_token +"&openid="+openid;
            Map<String, Object> map2 = HttpUtils.sendGet(wxurl);
            Object nickname = map2.get("nickname");
            Object headimgurl = map2.get("headimgurl");
            wechatUser.setAvatar(headimgurl.toString());
            wechatUser.setNickname(nickname.toString());
            wechatUser.setFanscount(0);
            wechatUser.setLastdate(new Date());
            wechatUser.setUpdatedate(new Date());
            userDao.insert(wechatUser);
            return user;
        }
    }

    @Override
    public int registerWechatNo(String code, User user) {
        WechatUser wechatUser = new WechatUser();
        String atUtl = WeChatFiles.accessTokenUrl + "?code="+code+"&appid="+ WeChatFiles.appid+"&secret="+ WeChatFiles.secret+"&grant_type=authorization_code";
        Map<String, Object> map1 = HttpUtils.sendGet(atUtl);
        Object access_token = map1.get("access_token");
        Object openid = map1.get("openid").toString();
        if(access_token == null && openid == null) {
           return 0;
        }
        wechatUser.setOpenid(openid.toString());
        String wxurl = wxInfoUrl + "?access_token=" + access_token +"&openid="+openid;
        Map<String, Object> map2 = HttpUtils.sendGet(wxurl);
        Object nickname = map2.get("nickname");
        Object headimgurl = map2.get("headimgurl");
        wechatUser.setAvatar(headimgurl.toString());
        wechatUser.setNickname(nickname.toString());
        wechatUser.setFanscount(0);
        wechatUser.setLastdate(new Date());
        wechatUser.setUpdatedate(new Date());
        wechatUser.setPeUserId(user.getId());
        userDao.insert(wechatUser);
        return 1;
    }
}
