package com.itheima.server.system.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.DeptMapper;
import com.itheima.domain.system.Dept;
import com.itheima.server.system.DeptServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServerImp implements DeptServer {

    @Autowired
    private DeptMapper mapper;


    public PageInfo<Dept> findAll(String companyId,int page, int size) {
        PageHelper.startPage(page,size);
        List<Dept> all = mapper.findAll(companyId);
        return new PageInfo<Dept>(all);
    }

    public List<Dept> findAllByCompanyId(String companyId) {
        return mapper.findAll(companyId);
    }

    public Dept findById(String id) {
        return mapper.findById(id);
    }

    public void update(Dept dept) {
        mapper.update(dept);
    }

    public void save(Dept dept) {
        String uuidID = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        dept.setId(uuidID);
        mapper.save(dept);
    }

    public void delete(String id) {
        mapper.delete(id);
    }
}
