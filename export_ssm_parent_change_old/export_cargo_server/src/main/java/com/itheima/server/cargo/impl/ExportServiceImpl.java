package com.itheima.server.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.cargo.*;
import com.itheima.domain.cargo.*;
import com.itheima.domain.vo.ExportProductResult;
import com.itheima.domain.vo.ExportProductVo;
import com.itheima.domain.vo.ExportResult;
import com.itheima.domain.vo.ExportVo;
import com.itheima.server.cargo.ExportService;
import com.itheima.server.cargo.utils.WebServerUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ExportProductDao exportProductDao;

    @Autowired
    private ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        String exportId = UtilFuns.generateId();
        export.setId(exportId);
        export.setState(0);
        export.setInputDate(new Date());
        String[] contractIds = export.getContractIds().split(",");
        StringBuilder builder = new StringBuilder();
        for (String str : contractIds) {
            builder.append(str + " ");
            Contract contract = contractDao.selectByPrimaryKey(str);
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }
        builder.delete(builder.length() - 1, builder.length());
        String newContractIds = builder.toString();
        export.setContractIds(newContractIds);
        List<String> list = Arrays.asList(contractIds);
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdIn(list);
        List<ContractProduct> products = contractProductDao.selectByExample(contractProductExample);
        int extNum = 0;
        for (ContractProduct product : products) {
            ExportProduct exportProduct = new ExportProduct();
            BeanUtils.copyProperties(product, exportProduct, new String[]{"id"});
            exportProduct.setExportId(exportId);
            String exportProductId = UtilFuns.generateId();
            exportProduct.setId(exportProductId);
            exportProduct.setCreateTime(new Date());
            exportProduct.setUpdateTime(new Date());
            exportProductDao.insertSelective(exportProduct);
            List<ExtCproduct> extCproducts = product.getExtCproducts();
            for (ExtCproduct extCproduct : extCproducts) {
                extNum++;
                ExtEproduct extEproduct = new ExtEproduct();
                BeanUtils.copyProperties(extCproduct, extEproduct, new String[]{"id"});
                extEproduct.setExportId(exportId);
                extEproduct.setExportProductId(exportProductId);
                extEproduct.setId(UtilFuns.generateId());
                extEproduct.setCreateTime(new Date());
                extEproduct.setUpdateTime(new Date());
                extEproductDao.insertSelective(extEproduct);
            }
        }
        export.setProNum(products.size());
        export.setExtNum(extNum);
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        exportDao.updateByPrimaryKeySelective(export);
        if (export.getExportProducts() != null) {
            List<ExportProduct> exportProducts = export.getExportProducts();
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }

    }

    @Override
    public void delete(String id) {
        Export export = exportDao.selectByPrimaryKey(id);
        String[] contractIds = export.getContractIds().split(" ");
        for (String strId : contractIds) {
            Contract contract = contractDao.selectByPrimaryKey(strId);
            contract.setState(1);
            contract.setUpdateTime(new Date());
            contractDao.updateByPrimaryKeySelective(contract);
        }
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(export.getId());
        List<ExportProduct> exportProducts = exportProductDao.selectByExample(exportProductExample);
        for (ExportProduct exportProduct : exportProducts) {
            exportProductDao.deleteByPrimaryKey(exportProduct.getId());
        }
        ExtEproductExample extEproductExample = new ExtEproductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExtEproduct> extEproducts = extEproductDao.selectByExample(extEproductExample);
        if (extEproducts != null) {
            for (ExtEproduct extEproduct : extEproducts) {
                extEproductDao.deleteByPrimaryKey(extEproduct.getId());
            }
        }
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<Export> exports = exportDao.selectByExample(example);
        return new PageInfo<Export>(exports);
    }

    //电子报运
    @Override
    public void saveByWebServer(String[] id) {
        for (String strId : id) {
            ExportVo exportVo = new ExportVo();
            Export export = exportDao.selectByPrimaryKey(strId);
            BeanUtils.copyProperties(export, exportVo, new String[]{"id"});
            exportVo.setExportId(strId);
            exportVo.setId(strId);
            ExportProductExample productExample = new ExportProductExample();
            productExample.createCriteria().andExportIdEqualTo(strId);
            List<ExportProduct> exportProducts = exportProductDao.selectByExample(productExample);
            for (ExportProduct exportProduct : exportProducts) {
                ExportProductVo exportProductVo = new ExportProductVo();
                BeanUtils.copyProperties(exportProduct, exportProductVo, new String[]{"id"});
                exportProductVo.setExportId(strId);
                exportProductVo.setExportProductId(exportProduct.getId());
                exportProductVo.setEid(strId);
                exportProductVo.setId(UtilFuns.generateId());
                exportVo.getProducts().add(exportProductVo);
            }
            //有个问题，没办法保证多选的情况下全部成功或全部失败
            WebServerUtils.saveExportByWebServer(exportVo);

            //更改合同状态为待审核
            export.setState(3);
            exportDao.updateByPrimaryKeySelective(export);

            //更改购销合同状态
            String contractIds = export.getContractIds();
            String[] contractIdArr = null;
            if (contractIds.contains(" ")) {
                contractIdArr = contractIds.split(" ");
            } else if (contractIds.contains(",")) {
                contractIdArr = contractIds.split(",");
            }
            for (String strIds : contractIdArr) {
                Contract contract = contractDao.selectByPrimaryKey(strIds);
                contract.setState(3);
                contractDao.updateByPrimaryKeySelective(contract);
            }
        }
    }

    @Override
    public void updateState(String id) {
        //返回海关平台状态
        ExportResult exportResult = WebServerUtils.getExportResult(id);
        if (exportResult.getState() == 2) {
            //依据该ID查询出相应的订单并更改属性
            Export export = exportDao.selectByPrimaryKey(id);
            export.setRemark(exportResult.getRemark());
            export.setState(exportResult.getState());
            exportDao.updateByPrimaryKeySelective(export);
            //依据查询的ID查询出相应的商品并更改属性
            Set<ExportProductResult> products = exportResult.getProducts();
            for (ExportProductResult product : products) {
                ExportProduct exportProduct = exportProductDao.selectByPrimaryKey(product.getExportProductId());
                exportProduct.setTax(product.getTax());
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }

            //更改购销合同状态
            String contractIds = export.getContractIds();
            String[] contractIdArr = null;
            if (contractIds.contains(" ")) {
                contractIdArr = contractIds.split(" ");
            } else if (contractIds.contains(",")) {
                contractIdArr = contractIds.split(",");
            }
            for (String strIds : contractIdArr) {
                Contract contract = contractDao.selectByPrimaryKey(strIds);
                contract.setState(2);
                contractDao.updateByPrimaryKeySelective(contract);
            }
        }
    }

    @Override
    public List<Export> findAlllist(ExportExample exportExample) {
        return exportDao.selectByExample(exportExample);
    }
}
