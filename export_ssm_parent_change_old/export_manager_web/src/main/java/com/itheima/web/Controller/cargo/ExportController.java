package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.BeanMapUtils;
import com.itheima.common.utils.UtilFuns;
import com.itheima.domain.cargo.*;
import com.itheima.server.cargo.ContractService;
import com.itheima.server.cargo.ExportProductService;
import com.itheima.server.cargo.ExportService;
import com.itheima.server.cargo.ExtCproductService;
import com.itheima.web.Controller.baseClass.BaseController;
import com.itheima.web.exceptions.CustomException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ExportService service;

    @Reference
    private ContractService contractService;

    @Reference
    private ExportProductService exportProductService;

    @Reference
    private ExtCproductService extCproductService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        ExportExample exportExample = new ExportExample();
        exportExample.createCriteria().andCompanyIdEqualTo(companyId);
        PageInfo exports = service.findAll(exportExample, page, size);
        request.setAttribute("page",exports);
        return "cargo/export/export-list";
    }

    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        ContractExample contractExample = new ContractExample();
        contractExample.createCriteria().andStateEqualTo(1);
        PageInfo pageInfo = contractService.findAll(contractExample, page, size);
        request.setAttribute("page",pageInfo);
        return "cargo/export/export-contractList";
    }

    @RequestMapping("/toExport")
    public String toExport(String id){
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }

    @RequestMapping("/edit")
    public String edit(Export export){
        export.setCompanyId(companyId);
        export.setCompanyName(companyName);
        if (UtilFuns.isEmpty(export.getId())){
            export.setCreateBy(user.getId());
            export.setCreateDept(user.getDeptId());
            service.save(export);
        }else {
            service.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        service.delete(id);
        return "redirect:/cargo/export/contractList.do";
    }

    @RequestMapping("/submit")
    public String submit(String id){
        Export export = new Export();
        export.setState(1);
        export.setId(id);
        service.update(export);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(0);
        service.update(export);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id) throws Exception{
        Export export = service.findById(id);
        int state = export.getState();
        if (state==1||state==2){
            throw new CustomException("信息锁定，无权编辑");
        }else {
            ExportProductExample exportProductExample = new ExportProductExample();
            exportProductExample.createCriteria().andExportIdEqualTo(id);
            List<ExportProduct> exportProducts = exportProductService.findAll(exportProductExample);
            request.setAttribute("export",export);
            request.setAttribute("eps",exportProducts);
            return "cargo/export/export-update";
        }
    }

    @RequestMapping("/toView")
    public String toView(String id){
        Export export = service.findById(id);
        request.setAttribute("export",export);
        return "cargo/export/export-view";
    }

    @RequestMapping("/updateState")
    public String updateState(String id,int page,int size){
        service.updateState(id);
        return "redirect:/cargo/export/list.do?page="+page+"&size="+size;
    }



    @RequestMapping("/exportE")
    public String exportE(String[] id) throws Exception{
        for(String strId:id){
            Export export = service.findById(strId);
            if (export.getState()!=1){
                throw new CustomException("报运项状态不符，请检查后再试");
            }
        }
        service.saveByWebServer(id);
        return "redirect:/cargo/export/list.do";
    }
}
