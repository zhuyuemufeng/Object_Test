package com.itheima.server.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.cargo.PackingListDao;
import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.PackingList;
import com.itheima.domain.cargo.PackingListExample;
import com.itheima.server.cargo.ExportService;
import com.itheima.server.cargo.PackingListServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PackingListServerImpl implements PackingListServer {


    @Autowired
    private PackingListDao listDao;

    @Autowired
    private ExportService exportService;


    @Override
    public PageInfo<PackingList> findAll(int page, int size, String companyId) {
        PageHelper.startPage(page, size);
        PackingListExample example = new PackingListExample();
        example.createCriteria().andCompanyIdEqualTo(companyId);
        List<PackingList> packingLists = listDao.selectByExample(example);
        return new PageInfo<PackingList>(packingLists);
    }

    @Override
    public PackingList findById(String id) {
        return listDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(PackingList packingList) {
        if (packingList.getExportIds() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] strings = packingList.getExportIds().split(",");
            for (String str:strings){
                Export export = exportService.findById(str);
                stringBuilder.append(export.getLcno()).append(",");
            }
            String exportNos = stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length()).toString();
            packingList.setExportNos(exportNos);
        }
        packingList.setPackingListId(UtilFuns.generateId());
        packingList.setState(0);
        listDao.insert(packingList);
    }

    @Override
    public void update(PackingList packingList) {
        if (packingList.getExportIds() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] strings = packingList.getExportIds().split(",");
            for (String str:strings){
                Export export = exportService.findById(str);
                stringBuilder.append(export.getLcno()).append(",");
            }
            String exportNos = stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length()).toString();
            packingList.setExportNos(exportNos);
        }
        listDao.updateByPrimaryKey(packingList);
    }

    @Override
    public void delete(String id) {
        listDao.deleteByPrimaryKey(id);
    }
}
