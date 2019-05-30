package com.itheima.web.Login;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.server.system.ModuleServer;
import com.itheima.server.system.UserServer;
import com.itheima.web.Controller.baseClass.BaseController;
import com.itheima.web.shiro.MyUsernamePasswordToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {

    @Reference
    private UserServer server;

    @Reference
    private ModuleServer moduleServer;

	@RequestMapping(value = "/login",name = "用户登录")
	public String login(String email,String password,String open) {
	   try {
           Subject subject = SecurityUtils.getSubject();
           MyUsernamePasswordToken token = null;
           if (open!=null){
               token = new MyUsernamePasswordToken(email,password);
               token.setIsOpenid(open);
           }else {
               token = new MyUsernamePasswordToken(email,password);
           }
           subject.login(token);
           User user = (User) subject.getPrincipal();
           session.setAttribute("user",user);
           List<Module> userModule = moduleServer.findUserModule(user);
           session.setAttribute("modules",userModule);
           return "home/main";

       }catch (Exception e){
           request.setAttribute("error","登录名或密码错误");
           return "forward:login.jsp";
       }
	}

    /**
     *
     * if(UtilFuns.isEmpty(email)){
         return "forward:login.jsp";
         }else {
         User user = server.findByEmail(email);
         if (user==null||!password.equals(user.getPassword())){
         request.setAttribute("error","登录名或密码错误");
         return "forward:login.jsp";
         }else {
         List<Module> userModule = moduleServer.findUserModule(user);
         session.setAttribute("user",user);
         session.setAttribute("modules",userModule);
         return "home/main";
     }
     }
     */

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        //SecurityUtils.getSubject().logout();   //登出
        session.removeAttribute("user");
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
