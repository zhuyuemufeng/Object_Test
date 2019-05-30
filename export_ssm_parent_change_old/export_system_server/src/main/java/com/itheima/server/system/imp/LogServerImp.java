package com.itheima.server.system.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.system.LogMapper;
import com.itheima.domain.system.SysLog;
import com.itheima.server.system.LogServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class LogServerImp implements LogServer {

    @Autowired
    private LogMapper mapper;


    public PageInfo<SysLog> findAll(int page, int size, String companyId) {
        PageHelper.startPage(page,size);
        List<SysLog> all = mapper.findAll(companyId);
        return new PageInfo<SysLog>(all);
    }


    public void save(SysLog sysLog) {
        sysLog.setId(UtilFuns.generateId());
        mapper.save(sysLog);
    }
}
