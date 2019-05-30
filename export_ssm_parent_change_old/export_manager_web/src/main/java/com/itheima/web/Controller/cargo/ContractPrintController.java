package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.common.utils.DownloadUtil;
import com.itheima.common.utils.UtilFuns;
import com.itheima.domain.vo.ContractProductVo;
import com.itheima.server.cargo.ContractService;
import com.itheima.web.Controller.baseClass.BaseController;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractPrintController extends BaseController {

    @Reference
    private ContractService service;

    @RequestMapping("/print")
    public String toPrint(){
        return "cargo/print/contract-print";
    }

    @RequestMapping("/printExcel")
    public void printExcel(String inputDate, HttpServletResponse response) throws Exception{
        String excelPath = session.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
        String bigTitle = inputDate.replace("-0","-").replace("-","年")+"月";
        int nRows = 0;
        int nCell = 1;
        XSSFCell cell = null;
        XSSFRow row = null;
        XSSFSheet sheet = workbook.getSheetAt(0);
        row = sheet.getRow(nRows++);
        cell = row.getCell(nCell);
        cell.setCellValue(bigTitle+"出货表");
        row = sheet.getRow(++nRows);
        float heightInPoints = row.getHeightInPoints();
        XSSFCellStyle cellStyle1 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle2 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle3 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle4 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle5 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle6 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle7 = row.getCell(nCell++).getCellStyle();
        XSSFCellStyle cellStyle8 = row.getCell(nCell++).getCellStyle();
        nCell = 1;
        List<ContractProductVo> printDate = service.findExcelPrintDate(inputDate, companyId);
        System.out.println(printDate);
        for (ContractProductVo productVo:printDate){
            row = sheet.createRow(nRows++);
            row.setHeightInPoints(heightInPoints);

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(productVo.getCustomName());

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(productVo.getContractNo());

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle3);
            cell.setCellValue(productVo.getProductNo());

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle4);
            cell.setCellValue(productVo.getCnumber());

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle5);
            cell.setCellValue(productVo.getFactoryName());

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle6);
            cell.setCellValue(UtilFuns.dateTimeFormat(productVo.getDeliveryPeriod(),"yyyy-MM"));

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle7);
            cell.setCellValue(UtilFuns.dateTimeFormat(productVo.getShipTime(),"yyyy-MM"));

            cell = row.createCell(nCell++);
            cell.setCellStyle(cellStyle8);
            cell.setCellValue(productVo.getTradeTerms());

            nCell = 1;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        new DownloadUtil().download(out,response,bigTitle+"出货表.xlsx");
        out.close();
        workbook.close();
    }
}
