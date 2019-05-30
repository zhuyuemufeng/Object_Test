package com.itheima.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.company.Company;
import com.itheima.server.company.CompanyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {

    @Reference
    private CompanyServer server;

    @RequestMapping("/apply")
    public @ResponseBody String applyCompany(Company company){
        server.save(company);
        return "1";
    }
}
