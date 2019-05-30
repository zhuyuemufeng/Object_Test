package com.itheima.web.Controller.stat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.server.stat.StatService;
import com.itheima.web.Controller.baseClass.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;

    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-"+chartsType;
    }

    @RequestMapping("/factoryCharts")
    public @ResponseBody List factoryCharts(){
        List<Map> factorySellData = statService.findFactorySellData(companyId);
        return factorySellData;
    }

    @RequestMapping("/getSellData")
    public @ResponseBody List getSellData(){
        return statService.findProductSellData(companyId);
    }

    @RequestMapping("/getOnlineData")
    public @ResponseBody List onlineCharts(){
        return statService.findOnlineData(companyId);
    }
}
