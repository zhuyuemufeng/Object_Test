package com.itheima.server.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
import com.itheima.domain.system.Role;

import java.util.List;
import java.util.Map;

public interface RoleServer {

    PageInfo<Role> findAll(int page,int size,String companyId);

    Role findById(String roleId);

    void update(Role role);

    void save(Role role);

    void delete(String roleId);

    List<Map> findTreeList(String roleId);

    void updateModuleMapper(String roleId,String moduleIds);

    List<String> findRoleByUid(String userId);

    List<Role> findAllRole(String companyId);

}
