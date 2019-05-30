package com.itheima.server.system.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.system.RoleMapper;
import com.itheima.domain.system.Role;
import com.itheima.server.system.RoleServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@Service
public class RoleServerImp implements RoleServer {

    @Autowired
    private RoleMapper mapper;

    public PageInfo<Role> findAll(int page, int size, String companyId) {
        PageHelper.startPage(page,size);
        List<Role> all = mapper.findAll(companyId);
        return new PageInfo<Role>(all);
    }

    public Role findById(String roleId) {
        return mapper.findById(roleId);
    }

    public void update(Role role) {
        mapper.update(role);
    }

    public void save(Role role) {
        role.setId(UtilFuns.generateId());
        mapper.save(role);
    }

    public void delete(String roleId) {
        mapper.delete(roleId);
    }

    public List<Map> findTreeList(String roleId) {
        return mapper.findTreeList(roleId);
    }

    public void updateModuleMapper(String roleId, String moduleIds) {
        mapper.deleteModuleById(roleId);
        String[] strs = moduleIds.split(",");
        for (int i = 0; i < strs.length; i++) {
            mapper.saveModuleMapper(roleId,strs[i]);
        }
    }

    public List<String> findRoleByUid(String userId) {
        return mapper.findRoleByUid(userId);
    }

    public List<Role> findAllRole(String companyId) {
        return mapper.findAll(companyId);
    }
}
