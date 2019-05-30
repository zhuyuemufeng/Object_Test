package com.itheima.dao.system;

import com.itheima.domain.system.SysLog;

import java.util.List;

public interface LogMapper {

    List<SysLog> findAll(String companyId);

    void save(SysLog log);
}
