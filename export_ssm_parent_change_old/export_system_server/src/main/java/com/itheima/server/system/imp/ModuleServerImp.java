package com.itheima.server.system.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.system.ModuleMapper;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.server.system.ModuleServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ModuleServerImp implements ModuleServer {

    @Autowired
    private ModuleMapper mapper;


    public PageInfo<Module> findAll(int page, int size) {
        PageHelper.startPage(page,size);
        List<Module> all = mapper.findAll();
        return new PageInfo<Module>(all);
    }


    public Module findById(String id) {
        return mapper.findById(id);
    }


    public void update(Module module) {
        mapper.update(module);
    }


    public void save(Module module) {
        module.setId(UtilFuns.generateId());
        mapper.save(module);
    }


    public void delete(String id) {
        mapper.delete(id);
    }


    public List<Module> findAllModule() {
        return mapper.findAll();
    }


    public List<Module> findUserModule(User user) {
        if (user.getDegree() == 0) {
            return mapper.findSystemModule("0");
        } else if (user.getDegree() == 1) {
            return mapper.findSystemModule("1");
        } else {
            return mapper.findUserModule(user.getId());
        }
    }
}
