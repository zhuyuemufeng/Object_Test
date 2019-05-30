package com.itheima.web.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.ExportExample;
import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.vo.ExportProductResult;
import com.itheima.domain.vo.ExportResult;
import com.itheima.server.cargo.ContractService;
import com.itheima.server.cargo.ExportProductService;
import com.itheima.server.cargo.ExportService;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class ExportChange {

    @Reference
    private ExportService service;

    @Reference
    private ExportProductService exportProductService;

    @Reference
    private ContractService contractService;

    @Scheduled(cron = "0 50 21 * * ?")
    public void DateBaseUpdate(){
        ExportExample exportExample = new ExportExample();
        exportExample.createCriteria().andStateEqualTo(3L);
        List<Export> exportList = service.findAlllist(exportExample);
        for (Export export:exportList){
            ExportResult exportResult = WebClient.create("http://localhost:8081/ws/export/user/" + export.getId()).type(MediaType.APPLICATION_XML).encoding("UTF-8").get(ExportResult.class);
            if (exportResult.getState()!=2){
                continue;
            }else {
                export.setRemark(exportResult.getRemark());
                export.setState(exportResult.getState());
                service.update(export);
                Set<ExportProductResult> products = exportResult.getProducts();
                for (ExportProductResult productResult:products){
                    String exportProductId = productResult.getExportProductId();
                    ExportProduct exportProduct = exportProductService.findById(exportProductId);
                    exportProduct.setTax(productResult.getTax());
                    exportProductService.update(exportProduct);
                }

                String contractIds = export.getContractIds();
                String[] contractIdArr = null;
                if (contractIds.contains(" ")) {
                    contractIdArr = contractIds.split(" ");
                } else if (contractIds.contains(",")) {
                    contractIdArr = contractIds.split(",");
                }
                for (String strIds : contractIdArr) {
                    Contract contract = contractService.findById(strIds);
                    contract.setState(2);
                    contractService.update(contract);
                }
            }
        }
    }
}
