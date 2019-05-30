package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.DownloadUtil;
import com.itheima.common.utils.UtilFuns;
import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.ExportExample;
import com.itheima.domain.cargo.PackingList;
import com.itheima.server.cargo.ExportService;
import com.itheima.server.cargo.PackingListServer;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cargo/packing")
public class PackingListController extends BaseController{

    @Reference
    private PackingListServer packingListServer;

    @Reference
    private ExportService exportService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        PageInfo<PackingList> list = packingListServer.findAll(page, size, companyId);
        request.setAttribute("page",list);
        return "cargo/packing/packing-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        ExportExample exportExample = new ExportExample();
        exportExample.createCriteria().andStateEqualTo(2L);
        exportExample.createCriteria().andCompanyIdEqualTo(companyId);
        List<Export> list = exportService.findAlllist(exportExample);
        request.setAttribute("eps",list);
        return "cargo/packing/packing-add";
    }

    @RequestMapping("/edit")
    public String edit(PackingList packingList){
        packingList.setCompanyId(companyId);
        packingList.setCompanyName(companyName);
        packingList.setCreateTime(new Date());
        packingList.setCreateBy(user.getUserName());
        packingList.setCreateDept(user.getDeptName());
        System.out.println(packingList.getExportNos());
        if (UtilFuns.isEmpty(packingList.getPackingListId())){
            packingListServer.save(packingList);
        }else {
            packingListServer.update(packingList);
        }
        return "redirect:/cargo/packing/list.do";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        PackingList packingList = packingListServer.findById(id);
        ExportExample exportExample = new ExportExample();
        exportExample.createCriteria().andStateEqualTo(2L);
        exportExample.createCriteria().andCompanyIdEqualTo(companyId);
        List<Export> list = exportService.findAlllist(exportExample);
        request.setAttribute("packingList",packingList);
        request.setAttribute("eps",list);
        return "cargo/packing/packing-update";
    }

    @RequestMapping("/submit")
    public String submit(String id){
        PackingList packingList = packingListServer.findById(id);
        packingList.setState(1);
        packingListServer.update(packingList);
        return "redirect:/cargo/packing/list.do";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        PackingList packingList = packingListServer.findById(id);
        packingList.setState(0);
        packingListServer.update(packingList);
        return "redirect:/cargo/packing/list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        packingListServer.delete(id);
        return "redirect:/cargo/packing/list.do";
    }

    @RequestMapping("/toLoad")
    public void toLoad(String id, HttpServletResponse response) throws Exception{
        PackingList packingList = packingListServer.findById(id);
        String realPath = session.getServletContext().getRealPath("/make/xlsprint/packing.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(realPath));
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;
        row = sheet.getRow(3);
        cell = row.getCell(0);
        cell.setCellValue(packingList.getSeller());
        cell = row.getCell(6);
        cell.setCellValue(packingList.getSeller());
        row = sheet.getRow(8);
        cell = row.getCell(0);
        cell.setCellValue(packingList.getBuyer());
        row = sheet.getRow(15);
        cell = row.getCell(0);
        cell.setCellValue(packingList.getInvoiceNo());
        cell = row.getCell(3);
        cell.setCellValue(packingList.getInvoiceDate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        Date createTime = packingList.getCreateTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");
        String format = dateFormat.format(createTime);
        new DownloadUtil().download(out,response,format+"装箱单.xls");
        out.close();
        workbook.close();
    }
}
