package com.itheima.web.Controller.baseClass;

import com.itheima.domain.system.User;
import com.itheima.server.company.CompanyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    protected HttpServletRequest request;

    protected String companyId;

    protected String companyName;

    protected HttpSession session;

    protected User user;

    @ModelAttribute
    public void requsetInit(HttpServletRequest request,HttpSession session){
        this.request = request;
        this.companyId = "1";
        this.companyName = "传智播客";
        this.session = session;
        this.user = (User)session.getAttribute("user");
    }
}
