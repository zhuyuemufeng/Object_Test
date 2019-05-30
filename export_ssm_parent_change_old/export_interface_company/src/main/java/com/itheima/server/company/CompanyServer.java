package com.itheima.server.company;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;

import java.util.List;

public interface CompanyServer {

    PageInfo<Company> findAll(int page, int size);

    Company findById(String id);

    void save(Company company);

    void update(Company company);

    void delete(String id);
}
