package com.itheima.dao.system;

import com.itheima.domain.system.Dept;

import java.util.List;

public interface DeptMapper {

    List<Dept> findAll(String companyId);

    Dept findById(String id);

    void update(Dept dept);

    void save(Dept dept);

    void delete(String id);
}
