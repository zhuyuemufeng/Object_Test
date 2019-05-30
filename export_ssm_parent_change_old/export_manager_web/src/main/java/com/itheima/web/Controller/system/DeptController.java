package com.itheima.web.Controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.server.system.DeptServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {
    //线程不安全

    @Reference
    private DeptServer server;

    @RequiresPermissions("部门管理")
    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        PageInfo<Dept> all = server.findAll(companyId, page, size);
        request.setAttribute("page",all);
        return "system/dept/dept-list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        List<Dept> allByCompanyId = server.findAllByCompanyId(companyId);
        Dept byId = server.findById(id);
        request.setAttribute("deptList",allByCompanyId);
        request.setAttribute("dept",byId);
        return "system/dept/dept-update";
    }

    @RequestMapping("/edit")
    public String edit(Dept dept){
        if (!StringUtils.isEmpty(dept.getId())) {
            Dept byId = server.findById(dept.getId());
            BeanUtils.copyProperties(dept, byId, new String[]{"companyId", "companyName"});
            server.update(byId);
        }else {
            dept.setCompanyName(companyName);
            dept.setCompanyId(companyId);
            server.save(dept);
        }
        return "redirect:/system/dept/list.do";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        List<Dept> allByCompanyId = server.findAllByCompanyId(companyId);
        request.setAttribute("deptList",allByCompanyId);
        return "system/dept/dept-add";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        server.delete(id);
        return "redirect:/system/dept/list.do";
    }
}
