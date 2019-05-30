package com.itheima.web.Controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;
import com.itheima.domain.system.User;
import com.itheima.server.system.LogServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/log")
public class LogController extends BaseController{

    @Reference
    private LogServer server;

    @RequiresPermissions("日志管理")
    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        User user = (User)session.getAttribute("user");
        PageInfo<SysLog> all = server.findAll(page, size, user.getCompanyId());
        request.setAttribute("page",all);
        return "system/log/log-list";
    }

}
