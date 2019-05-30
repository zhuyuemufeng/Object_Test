package com.itheima.server.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.beanUtils.FileUploadUtil;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.server.cargo.ContractService;
import com.itheima.server.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Override
    public void save(ExtCproduct extCproduct) {
        extCproduct.setUpdateTime(new Date());
        extCproduct.setCreateTime(new Date());
        extCproduct.setId(UtilFuns.generateId());
        Integer cnumber = extCproduct.getCnumber();
        Double price = extCproduct.getPrice()*cnumber;
        extCproduct.setAmount(price);
        extCproductDao.insertSelective(extCproduct);
        String contractId = extCproduct.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        contract.setUpdateTime(new Date());
        contract.setExtNum(contract.getExtNum()+cnumber);
        contract.setTotalAmount(contract.getTotalAmount()+price);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        extCproduct.setUpdateTime(new Date());
        Integer cnumber = extCproduct.getCnumber();
        Double price = extCproduct.getPrice()*cnumber;
        extCproduct.setAmount(price);
        ExtCproduct extP = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //计算数量差值
        int diffrentNum = extCproduct.getCnumber()-extP.getCnumber();
        //计算价格差值
        Double diffrentPrice = ((extCproduct.getCnumber())*extCproduct.getPrice())-((extP.getCnumber())*extP.getPrice());
        contract.setExtNum(contract.getExtNum()+diffrentNum);
        contract.setTotalAmount(contract.getTotalAmount()+diffrentPrice);
        contract.setUpdateTime(new Date());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double diffrentPrice = extCproduct.getPrice()*extCproduct.getCnumber();
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-diffrentPrice);
        contract.setExtNum(contract.getExtNum()-extCproduct.getCnumber());
        contractDao.updateByPrimaryKeySelective(contract);

        //云端备份删除
        fileUploadUtil.delete(extCproduct.getProductImage());
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ExtCproduct> byExample = extCproductDao.selectByExample(example);
        return new PageInfo<ExtCproduct>(byExample);
    }
}
