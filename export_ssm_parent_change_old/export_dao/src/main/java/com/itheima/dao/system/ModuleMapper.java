package com.itheima.dao.system;

import com.itheima.domain.system.Module;

import java.util.List;

public interface ModuleMapper {

    List<Module> findAll();

    Module findById(String id);

    void update(Module module);

    void save(Module module);

    void delete(String id);

    List<Module> findUserModule(String userId);

    List<Module> findSystemModule(String num);

    List<Module> findAdminModule();
}
