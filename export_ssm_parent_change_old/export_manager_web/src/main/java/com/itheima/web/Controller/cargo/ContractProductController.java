package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.beanUtils.FileUploadUtil;
import com.itheima.domain.cargo.*;
import com.itheima.domain.cargo.ContractProductExample.Criteria;
import com.itheima.server.cargo.ContractProductService;
import com.itheima.server.cargo.FactoryService;
import com.itheima.web.Controller.baseClass.BaseController;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService service;

    @Reference
    private FactoryService factoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("list")
    public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size, String contractId) {
        ContractProductExample example = new ContractProductExample();
        Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(contractId);
        PageInfo list = service.findAll(example, page, size);
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findList(factoryExample);
        request.setAttribute("page", list);
        request.setAttribute("contractId", contractId);
        request.setAttribute("factoryList", factoryList);
        return "cargo/product/product-list";
    }

    @RequestMapping("/edit")
    public String edit(ContractProduct product, MultipartFile productPhoto) throws Exception {
        product.setCompanyId(companyId);
        product.setCompanyName(companyName);
        String imgUrl = "";
        if (UtilFuns.isEmpty(product.getId())) {
            //文件上传非空判断productPhoto != null错误
            if (!UtilFuns.isEmpty(productPhoto.getOriginalFilename())) {
                String upload = fileUploadUtil.upload(productPhoto);
                imgUrl = "http://" + upload;
            }
            product.setProductImage(imgUrl);
            System.out.println("aaaa");
            service.save(product);
        } else {
            if (!UtilFuns.isEmpty(productPhoto.getOriginalFilename())) {
                ContractProduct contractProduct = service.findById(product.getId());
                String oldImgUrl = contractProduct.getProductImage();
                if (!UtilFuns.isEmpty(oldImgUrl)){
                    fileUploadUtil.delete(oldImgUrl);
                }
                String upload = fileUploadUtil.upload(productPhoto);
                imgUrl = "http://" + upload;
            }
            product.setProductImage(imgUrl);
            service.update(product);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId=" + product.getContractId();
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        ContractProduct product = service.findById(id);
        request.setAttribute("contractProduct", product);
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findList(factoryExample);
        request.setAttribute("factoryList", factoryList);
        return "cargo/product/product-update";
    }

    @RequestMapping("/delete")
    public String delete(String id, String contractId) {

        service.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        request.setAttribute("contractId", contractId);
        return "cargo/product/product-import";
    }

    @RequestMapping("/import")
    public String improtProduct(String contractId, MultipartFile file) throws IOException {
        if (!UtilFuns.isEmpty(file.getOriginalFilename())) {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            int fristRowNum = sheet.getFirstRowNum();
            List<ContractProduct> list = new ArrayList<>();
            for (int i = fristRowNum+1; i <= lastRowNum; i++) {
                XSSFRow sheetRow = sheet.getRow(i);
                //这个取值有点多余，固定模板固定值
                int fristCellNum = sheetRow.getFirstCellNum();
                int lastCellNum = sheetRow.getLastCellNum();
                ContractProduct product = new ContractProduct();
                product.setContractId(contractId);
                product.setCompanyName(companyName);
                product.setCompanyId(companyId);
                product.setId(UtilFuns.generateId());
                for (int j = fristCellNum; j < lastCellNum; j++) {
                    XSSFCell cell = sheetRow.getCell(j);
                    cell.setCellType(CellType.STRING);
                    if (j==1){
                        product.setFactoryName(cell.getStringCellValue());
                    }else if (j==2){
                        product.setProductNo(cell.getStringCellValue());
                    }else if (j==3){
                        product.setCnumber(Integer.parseInt(cell.getStringCellValue()));
                    }else if (j==4){
                        product.setPackingUnit(cell.getStringCellValue());
                    }else if (j==5){
                        product.setLoadingRate(cell.getStringCellValue());
                    }else if (j==6){
                        product.setBoxNum(Integer.parseInt(cell.getStringCellValue()));
                    }else if (j==7){
                        product.setPrice(Double.parseDouble(cell.getStringCellValue()));
                    }else if (j==8){
                        product.setProductDesc(cell.getStringCellValue());
                    }else if (j==9){
                        product.setProductRequest(cell.getStringCellValue());
                    }
                }
                list.add(product);
            }
            service.insertBatch(list);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }


    @RequestMapping("/toLoad")
    public void toLoad(HttpServletResponse response) throws Exception{
        String realPath = session.getServletContext().getRealPath("/excelLoad/上传货物模板.xlsx");
        String fileName = "上传货物模板.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        InputStream inputStream = new FileInputStream(realPath);
        byte[] arr = new byte[1024];
        int size = 0;
        while ((size = inputStream.read(arr))>0){
            outputStream.write(arr,0,size);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
