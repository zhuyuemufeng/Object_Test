package com.itheima.server.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;

import java.util.List;

public interface ModuleServer {

    PageInfo<Module> findAll(int page,int size);

    Module findById(String id);

    void update(Module module);

    void save(Module module);

    void delete(String id);

    List<Module> findAllModule();

    List<Module> findUserModule(User user);
}
