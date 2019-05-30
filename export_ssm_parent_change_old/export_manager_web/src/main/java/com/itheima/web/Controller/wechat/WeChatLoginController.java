package com.itheima.web.Controller.wechat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.User;
import com.itheima.domain.system.WechatUser;
import com.itheima.server.system.UserChatLogin;
import com.itheima.server.system.UserServer;
import com.itheima.staticClassFile.WeChatFiles;
import com.itheima.web.Controller.baseClass.BaseController;
import com.itheima.web.exceptions.CustomException;
import com.itheima.web.utils.URLEncodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/weChat")
public class WeChatLoginController extends BaseController {

    @Reference
    private UserChatLogin userChatLogin;

    @Reference
    private UserServer userServer;

    @RequestMapping("/getCode")
    @ResponseBody
    public void getCode(HttpServletResponse resp) throws Exception {
        //拼接url
        StringBuilder url = new StringBuilder();
        url.append("https://open.weixin.qq.com/connect/qrconnect?");
        //appid
        url.append("appid=" + WeChatFiles.appid);
        //回调地址 ,回调地址要进行Encode转码
        String redirect_uri = "http://note.java.itcast.cn/weChat/callBack.do";
        //转码
        url.append("&redirect_uri=" + URLEncodeUtils.toURLEncoded(redirect_uri));
        url.append("&response_type=code");
        url.append("&scope=snsapi_login&state=STATE#wechat_redirect");
        String toUrl = url.toString();
        resp.sendRedirect(toUrl);
    }


    @RequestMapping("/callBack")
    public String callback(String code) throws Exception {
        if (user == null) {
            WechatUser user = userChatLogin.Login(code);
            if (user == null) {
                request.setAttribute("error", "微信认证失败");
                return "forward:/login.jsp";
            }
            String userPeUserId = user.getPeUserId();
            User userServers = userServer.findById(userPeUserId);
                return "redirect:/login.do?email="+userServers.getEmail()+"&password="+userServers.getPassword()+"&open=1";
        }else {
            int i = userChatLogin.registerWechatNo(code, user);
            if (i==0){
                throw new CustomException("微信绑定失败");
            }else {
                return "redirect:/system/user/list.do";
            }
        }
    }
}
