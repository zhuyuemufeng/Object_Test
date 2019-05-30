package com.itheima.dao.stat;

import java.util.List;
import java.util.Map;

/**
 * 统计图表的持久层接口
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface StatDaoMapper {

    /**
     * 查询生产厂家的销售统计
     * @param companyId
     * @return
     */
    List<Map> findFactorySellData(String companyId);

    /**
     * 查询商品销售情况统计
     * @param companyId
     * @return
     */
    List<Map> findProductSellData(String companyId);

    /**
     * 查询在线人数情况统计
     * @param companyId
     * @return
     */
    List<Map> findOnlineData(String companyId);
}
