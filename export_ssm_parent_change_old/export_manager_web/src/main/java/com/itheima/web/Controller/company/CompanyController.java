package com.itheima.web.Controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
import com.itheima.server.company.CompanyServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Reference
    private CompanyServer server;

    @RequiresPermissions("企业管理")
    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        System.out.println(page);
        System.out.println(size);
        PageInfo<Company> all = server.findAll(page, size);
        request.setAttribute("page",all);
        return "company/company-list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Company byId = server.findById(id);
        request.setAttribute("company",byId);
        return "company/company-update";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "company/company-add";
    }

    @RequestMapping("/edit")
    public String edit(Company company){
        if (StringUtils.isEmpty(company.getId())){
            server.save(company);
        }else {
            server.update(company);
        }
        return "redirect:list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        server.delete(id);
        return "redirect:list.do";
    }
}
