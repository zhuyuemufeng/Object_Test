package com.itheima.server.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;

import java.util.List;

public interface LogServer {

    PageInfo<SysLog> findAll(int page, int size, String companyId);

    void save(SysLog sysLog);
}
