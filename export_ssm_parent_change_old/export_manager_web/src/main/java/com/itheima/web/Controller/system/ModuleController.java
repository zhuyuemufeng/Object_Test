package com.itheima.web.Controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.domain.system.Module;
import com.itheima.server.system.ModuleServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController{

    @Reference
    private ModuleServer server;

    @RequiresPermissions("模块管理")
    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        PageInfo<Module> all = server.findAll(page, size);
        request.setAttribute("page",all);
        return "system/module/module-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        List<Module> allModule = server.findAllModule();
        request.setAttribute("menus",allModule);
        return "system/module/module-add";
    }

    @RequestMapping(value = "/edit")
    public String edit(Module module){
        if (UtilFuns.isEmpty(module.getId())){
            server.save(module);
        }else {
            server.update(module);
        }
        return "redirect:/system/module/list.do";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Module byId = server.findById(id);
        List<Module> allModule = server.findAllModule();
        request.setAttribute("module",byId);
        request.setAttribute("menus",allModule);
        return "system/module/module-update";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        server.delete(id);
        return "redirect:/system/module/list.do";
    }
}
