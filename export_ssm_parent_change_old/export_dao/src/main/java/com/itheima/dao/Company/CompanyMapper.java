package com.itheima.dao.Company;

import com.itheima.domain.company.Company;

import java.util.List;

public interface CompanyMapper {

    List<Company> findAll();

    Company findById(String id);

    void save(Company company);

    void update(Company company);

    void delete(String id);

}
