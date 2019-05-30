package com.itheima.web.Controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.domain.system.Role;
import com.itheima.server.system.RoleServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Reference
    private RoleServer server;

    @RequiresPermissions("角色管理")
    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        PageInfo<Role> all = server.findAll(page, size, companyId);
        request.setAttribute("page",all);
        return "system/role/role-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "system/role/role-add";
    }

    @RequestMapping("/edit")
    public String edit(Role role){
        role.setCompanyId(companyId);
        role.setCompanyName(companyName);
        if (UtilFuns.isEmpty(role.getId())){
            server.save(role);
        }else {
            server.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String roleId){
        Role byId = server.findById(roleId);
        request.setAttribute("role",byId);
        return "system/role/role-update";
    }

    @RequestMapping("/delete")
    public String delete(String roleId){
        server.delete(roleId);
        return "redirect:/system/role/list.do";
    }

    @RequestMapping("/roleModule")
    public String roleModule(String roleId){
        Role byId = server.findById(roleId);
        request.setAttribute("role",byId);
        return "system/role/role-module";
    }

    @RequestMapping("/initModuleData")
    public @ResponseBody List initModuleData(String roleId){
        List<Map> treeList = server.findTreeList(roleId);
        return treeList;
    }

    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String moduleIds,String roleId){
        server.updateModuleMapper(roleId,moduleIds);
        return "redirect:/system/role/list.do";
    }
}
