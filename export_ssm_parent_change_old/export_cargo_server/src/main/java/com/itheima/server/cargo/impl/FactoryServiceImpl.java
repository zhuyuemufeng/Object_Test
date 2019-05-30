package com.itheima.server.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.cargo.FactoryDao;
import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import com.itheima.server.cargo.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryDao factoryDao;

    @Override
    public void save(Factory factory) {
        factory.setId(UtilFuns.generateId());
        factoryDao.insertSelective(factory);
    }

    @Override
    public void update(Factory factory) {
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);
    }

    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Factory> findList(FactoryExample example) {
        return factoryDao.selectByExample(example);
    }

    @Override
    public PageInfo<Factory> findAll(int page,int size) {
        PageHelper.startPage(page,size);
        FactoryExample example = new FactoryExample();
        example.createCriteria().andIdIsNotNull();
        List<Factory> factories = factoryDao.selectByExample(example);
        return new PageInfo<Factory>(factories);
    }
}
