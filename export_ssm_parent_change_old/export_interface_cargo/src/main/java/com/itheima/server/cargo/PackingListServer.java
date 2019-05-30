package com.itheima.server.cargo;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.PackingList;

public interface PackingListServer {

    PageInfo<PackingList> findAll(int page,int size,String companyId);

    PackingList findById(String id);

    void save(PackingList packingList);

    void update(PackingList packingList);

    void delete(String id);
}
