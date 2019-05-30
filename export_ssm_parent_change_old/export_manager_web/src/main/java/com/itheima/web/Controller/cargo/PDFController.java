package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.common.utils.BeanMapUtils;
import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.cargo.ExportProductExample;
import com.itheima.server.cargo.ExportProductService;
import com.itheima.server.cargo.ExportService;
import com.itheima.web.Controller.baseClass.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cargo/export")
public class PDFController extends BaseController {

    @Reference
    private ExportService service;

    @Reference
    private ExportProductService exportProductService;

    //下载PDF报运单
    @RequestMapping("/exportPdf")
    public void exportPdf(String id, HttpServletResponse response) throws Exception {
        //准备数据
        Export export = service.findById(id);
        Map<String, Object> map = BeanMapUtils.beanToMap(export);
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(example);

        //读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/export.jasper");
        InputStream in = new FileInputStream(realPath);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(exportProducts);

        //输出对象构建
        JasperPrint jasperPrint = JasperFillManager.fillReport(in,map,jrDataSource);

        //输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
        in.close();
    }
}
