package com.itheima.dao.system;

import com.itheima.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper {

    List<Role> findAll(String companyId);

    Role findById(String roleId);

    void update(Role role);

    void save(Role role);

    void delete(String roleId);

    List<Map> findTreeList(String roleId);

    void deleteModuleById(String roleId);

    void saveModuleMapper(@Param("roleId") String roleId,@Param("moduleId") String moduleId);

    List<String> findRoleByUid(String userId);

}
