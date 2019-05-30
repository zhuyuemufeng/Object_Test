package com.itheima.server.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.User;

import java.util.List;

public interface UserServer {

    PageInfo<User> findAll(int page, int size, String companyId);

    User findById(String userId);

    int delete(String userId);

    int save(User user);

    int update(User user);

    void updateUserRoles(String[] rolesId,String userId);

    User findByEmail(String email);
}
