package com.itheima.web.Controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.system.DeptMapper;
import com.itheima.dao.system.UserMapper;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import com.itheima.server.system.DeptServer;
import com.itheima.server.system.RoleServer;
import com.itheima.server.system.UserServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Reference
    private DeptServer deptService;

    @Reference
    private UserServer userService;

    @Reference
    private RoleServer roleServer;

    @RequiresPermissions("用户管理")
    @RequestMapping(value = "/list",name = "用户列表")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size){

        PageInfo pageInfo = userService.findAll(page,size,companyId);
        request.setAttribute("page",pageInfo);
        return "system/user/user-list";
    }

    @RequestMapping(value = "/toAdd",name = "跳转用户新增页面")
    public String toAdd(){
        List<Dept> deptList = deptService.findAllByCompanyId(companyId);
        request.setAttribute("deptList",deptList);
        return "system/user/user-add";
    }

    @RequestMapping(value = "/edit",name = "用户修改或保存")
    public String edit(User user) {
        if (StringUtils.isEmpty(user.getId())) {
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            userService.save(user);
        } else {
            User dbUser = userService.findById(user.getId());
            BeanUtils.copyProperties(user, dbUser, new String[]{"companyId", "companyName"});
            userService.update(dbUser);
        }
        return "redirect:/system/user/list.do";
    }
    @RequestMapping(value = "/toUpdate",name = "跳转用户修改页面")
    public String toUpdate(String id) {
        //1.查询用户信息
        User user = userService.findById(id);
        request.setAttribute("user",user);
        //2.查询部门列表
        List<Dept> deptList = deptService.findAllByCompanyId(companyId);
        request.setAttribute("deptList",deptList);
        return "system/user/user-update";
    }
    @RequestMapping(value = "/delete",name = "删除用户")
    public String delete(String id){
        userService.delete(id);
        return "redirect:/system/user/list.do";
    }

    @RequestMapping(value = "/roleList",name = "跳转角色分配页面")
    public String roleList(String id){
        List<String> roleByUid = roleServer.findRoleByUid(id);
        List<Role> allRole = roleServer.findAllRole(companyId);
        User byId = userService.findById(id);
        request.setAttribute("roleList",allRole);
        request.setAttribute("userRoleStr",roleByUid.toString());
        request.setAttribute("user",byId);
        return "system/user/user-role";
    }


    @RequestMapping(value = "/changeRole",name = "修改角色")
    public String changeRole(String[] roleIds,String userid){
        userService.updateUserRoles(roleIds,userid);
        return "redirect:/system/user/list.do";
    }
}
