package com.itheima.server.company.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.Company.CompanyMapper;
import com.itheima.domain.company.Company;
import com.itheima.server.company.CompanyServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServerImp implements CompanyServer {

    @Autowired
    private CompanyMapper mapper;

    public PageInfo<Company> findAll(int page, int size) {
        PageHelper.startPage(page,size);
        List<Company> list = mapper.findAll();
        PageInfo<Company> companyPageInfo = new PageInfo<>(list);
        System.out.println(companyPageInfo.getEndRow());
        return companyPageInfo;
    }

    public Company findById(String id) {
        return mapper.findById(id);
    }

    public void save(Company company) {
        String uId = UUID.randomUUID().toString().replace("-","").toUpperCase();
        company.setId(uId);
        mapper.save(company);
    }

    public void update(Company company) {
        mapper.update(company);
    }

    public void delete(String id) {
        mapper.delete(id);
    }
}
