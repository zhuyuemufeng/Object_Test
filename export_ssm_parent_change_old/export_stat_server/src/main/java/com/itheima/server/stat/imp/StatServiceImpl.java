package com.itheima.server.stat.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.stat.StatDaoMapper;
import com.itheima.server.stat.StatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDaoMapper mapper;

    @Override
    public List<Map> findFactorySellData(String companyId) {
        return mapper.findFactorySellData(companyId);
    }

    @Override
    public List<Map> findProductSellData(String companyId) {
        return mapper.findProductSellData(companyId);
    }

    @Override
    public List<Map> findOnlineData(String companyId) {
        return mapper.findOnlineData(companyId);
    }
}
