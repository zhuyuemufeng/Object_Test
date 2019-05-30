package com.itheima.server.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.cargo.ExportProductDao;
import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.cargo.ExportProductExample;
import com.itheima.server.cargo.ExportProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExportProductServiceImpl implements ExportProductService {

    @Autowired
    private ExportProductDao productDao;

    @Override
    public ExportProduct findById(String id) {
        return productDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExportProduct exportProduct) {
        exportProduct.setId(UtilFuns.generateId());
        productDao.insertSelective(exportProduct);
    }

    @Override
    public void update(ExportProduct exportProduct) {
        productDao.updateByPrimaryKeySelective(exportProduct);

    }

    @Override
    public void delete(String id) {
        productDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample exportProductExample) {
        return productDao.selectByExample(exportProductExample);
    }


}
