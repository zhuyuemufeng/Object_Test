package com.itheima.dao.system;


import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

	List<User> findAll(String companyId);

    User findById(String userId);

	int delete(String userId);

	int save(User user);

	int update(User user);

	void deleteUserRoles(String userId);

	void saveUserRoles(@Param("userId") String userId, @Param("roleId") String roleId);

	User findByEmail(String email);
}