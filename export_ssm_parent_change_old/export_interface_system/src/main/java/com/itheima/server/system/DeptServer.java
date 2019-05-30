package com.itheima.server.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;

import java.util.List;

public interface DeptServer {

    PageInfo<Dept> findAll(String companyId,int page,int size);

    List<Dept> findAllByCompanyId(String companyId);

    Dept findById(String id);

    void update(Dept dept);

    void save(Dept dept);

    void delete(String id);
}
