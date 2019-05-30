package com.itheima.server.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.beanUtils.FileUploadUtil;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.*;
import com.itheima.server.cargo.ContractProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao productDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private FileUploadUtil fileUploadUtil;


    @Override
    public void save(ContractProduct contractProduct) {
        contractProduct.setId(UtilFuns.generateId());
        double acount = (contractProduct.getPrice())*(contractProduct.getCnumber());
        contractProduct.setAmount(acount);
        productDao.insertSelective(contractProduct);
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()+acount);
        contract.setProNum(contract.getProNum()+contractProduct.getCnumber());
        contract.setUpdateTime(new Date());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        ContractProduct oldProcuct = productDao.selectByPrimaryKey(contractProduct.getId());
        Double oldAcount = oldProcuct.getAmount();
        double newAcount = contractProduct.getPrice()*contractProduct.getCnumber();
        contractProduct.setAmount(newAcount);
        productDao.updateByPrimaryKeySelective(contractProduct);
        Double acount = newAcount-oldAcount;
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()+acount);
        contractDao.updateByPrimaryKeySelective(contract);

    }

    @Override
    public void delete(String id) {
        //更新对应合同数据，并删除对应附件信息
        ContractProduct oldProduct = productDao.selectByPrimaryKey(id);
        double aCount = oldProduct.getAmount();
        ExtCproductExample extCproduct = new ExtCproductExample();
        extCproduct.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> list = extCproductDao.selectByExample(extCproduct);
        int exNum = 0;
        double exAcount = 0.0;
        for (ExtCproduct ex:list){
            exNum +=ex.getCnumber();
            exAcount += ex.getAmount();
        }
        Contract contract = contractDao.selectByPrimaryKey(oldProduct.getContractId());
        contract.setUpdateTime(new Date());
        contract.setExtNum(contract.getExtNum()-exNum);
        contract.setProNum(contract.getProNum()-oldProduct.getCnumber());
        contract.setTotalAmount(contract.getTotalAmount()-exAcount-oldProduct.getAmount());
        contractDao.updateByPrimaryKeySelective(contract);
        ExtCproduct extCproduct1 = new ExtCproduct();
        extCproduct1.setContractProductId(id);
        productDao.deleteByPrimaryKey(id);
        extCproductDao.deleteByExtCproduct(extCproduct1);

        //删除云端备份图片
        String oldImge = oldProduct.getProductImage();
        fileUploadUtil.delete(oldImge);
        for (ExtCproduct extCpro:list){
            fileUploadUtil.delete(extCpro.getProductImage());
        }

    }

    @Override
    public ContractProduct findById(String id) {
        return productDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ContractProduct> contractProducts = productDao.selectByExample(example);
        return new PageInfo(contractProducts);
    }

    @Override
    public void insertBatch(List<ContractProduct> products) {
        for (ContractProduct product:products){
            save(product);
        }
    }
}
