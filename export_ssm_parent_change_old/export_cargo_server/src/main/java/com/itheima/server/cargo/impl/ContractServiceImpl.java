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
import com.itheima.domain.vo.ContractProductVo;
import com.itheima.server.cargo.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractProductDao productDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        //设置起始金额
        contract.setTotalAmount(0.0);
        //设置起始货物数量
        contract.setProNum(0);
        //设置起始附件数量
        contract.setExtNum(0);
        //设置起始合同状态
        contract.setState(0);
        //设置合同创建时间
        contract.setCreateTime(new Date());
        //设置合同更新时间，初始值与创建时间保持一致
        contract.setUpdateTime(new Date());
        //设置合同ID，采用UUID
        contract.setId(UtilFuns.generateId());
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        //重置合同更新时间
        contract.setUpdateTime(new Date());
       contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //更具ID删除合同
        contractDao.deleteByPrimaryKey(id);
        //创建货物和附件删除对象，设置相应条件
        ContractProduct product = new ContractProduct();
        ExtCproduct extCproduct = new ExtCproduct();
        product.setContractId(id);
        extCproduct.setContractId(id);
        //将云端存储图片删除提前，防止数据取不到
        //查询id的货物和附件
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdEqualTo(id);
        List<ContractProduct> contractProducts = productDao.selectByExample(contractProductExample);
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        List<String> list = new ArrayList<>();
        String imgUrl = null;
        //将货物的商品图片和附件的商品图片存入list集合中
        for (ContractProduct contractProduct:contractProducts){
            imgUrl = contractProduct.getProductImage();
            if (!UtilFuns.isEmpty(imgUrl)){
                list.add(imgUrl);
            }
        }
        for (ExtCproduct extCp:extCproducts){
            imgUrl = extCp.getProductImage();
            if (!UtilFuns.isEmpty(imgUrl)){
                list.add(imgUrl);
            }
        }
        //传入工具类进行批量删除
        fileUploadUtil.batchDelete(list);
        //删除货物和附件
        productDao.deleteByContractProduct(product);
        extCproductDao.deleteByExtCproduct(extCproduct);
    }

    @Override
    public PageInfo findAll(ContractExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<Contract> contracts = contractDao.selectByExample(example);
        return new PageInfo<Contract>(contracts);
    }

    @Override
    public List<ContractProductVo> findExcelPrintDate(String dateTime,String companyId) {
        return contractDao.findExcelPrintDate(dateTime,companyId);
    }
}
