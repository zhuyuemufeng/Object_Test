package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.beanUtils.FileUploadUtil;
import com.itheima.domain.cargo.*;
import com.itheima.server.cargo.ExtCproductService;
import com.itheima.server.cargo.FactoryService;
import com.itheima.web.Controller.baseClass.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService service;

    @Reference
    private FactoryService factoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/list")
    public String findAll(String contractId, String contractProductId, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo pageInfo = service.findAll(extCproductExample, page, size);
        request.setAttribute("page",pageInfo);
        request.setAttribute("contractProductId",contractProductId);
        request.setAttribute("contractId",contractId);
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findList(factoryExample);
        request.setAttribute("factoryList",factoryList);
        return "cargo/extc/extc-list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId, String contractProductId){
        ExtCproduct extCproduct = service.findById(id);
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findList(factoryExample);
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("extCproduct",extCproduct);
        request.setAttribute("contractProductId",contractProductId);
        request.setAttribute("contractId",contractId);
        return "cargo/extc/extc-update";
    }

    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto) throws Exception{
        extCproduct.setCompanyId(companyId);
        extCproduct.setCompanyName(companyName);
        extCproduct.setUpdateTime(new Date());
        String imgUrl = "";
        if (UtilFuns.isEmpty(extCproduct.getId())) {
            if (!UtilFuns.isEmpty(productPhoto.getOriginalFilename())) {
                String upload = fileUploadUtil.upload(productPhoto);
                imgUrl = "http://" + upload;
            }
            extCproduct.setProductImage(imgUrl);
            service.save(extCproduct);
        } else {
            if (!UtilFuns.isEmpty(productPhoto.getOriginalFilename())) {
                //更新将原来云端旧照片进行删除，上传新的照片，并将路径更改
                ExtCproduct extCproduct1 = service.findById(extCproduct.getContractId());
                String oldImgUrl = extCproduct1.getProductImage();
                if (!UtilFuns.isEmpty(oldImgUrl)) {
                    fileUploadUtil.delete(oldImgUrl);
                }
                String upload = fileUploadUtil.upload(productPhoto);
                imgUrl = "http://" + upload;
            }
            extCproduct.setProductImage(imgUrl);
            service.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }

    @RequestMapping("delete")
    public String delete(String id,String contractId,String contractProductId){
        service.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
