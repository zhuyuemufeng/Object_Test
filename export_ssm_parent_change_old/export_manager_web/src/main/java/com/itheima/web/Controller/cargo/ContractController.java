package com.itheima.web.Controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.domain.cargo.ContractExample.Criteria;
import com.itheima.server.cargo.ContractService;
import com.itheima.web.Controller.baseClass.BaseController;
import com.itheima.web.exceptions.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService service;

    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size) throws CustomException {
        ContractExample example = new ContractExample();
        Criteria criteria = example.createCriteria();
        switch (user.getDegree()){
            case 0:
                throw new CustomException("您无权访问");
            case 1:
                criteria.andCompanyIdEqualTo(companyId);
                break;
            case 2:
                criteria.andCreateDeptLike(user.getDeptId()+"%");
                break;
            case 3:
                criteria.andCreateDeptEqualTo(user.getDeptId());
                break;
            case 4:
                criteria.andCreateByEqualTo(user.getId());
                break;
            default:
                throw new CustomException("未获取您的权限，请再次登录");
        }
        PageInfo all = service.findAll(example, page, size);
        request.setAttribute("page",all);
        return "cargo/contract/contract-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "cargo/contract/contract-add";
    }

    @RequestMapping("/edit")
    public String edit(Contract contract){
        contract.setCompanyId(companyId);
        contract.setCompanyName(companyName);
        contract.setUpdateTime(new Date());
        if (UtilFuns.isEmpty(contract.getId())){
            contract.setCreateBy(user.getId());
            contract.setCreateDept(user.getDeptId());
            contract.setCreateTime(new Date());
            service.save(contract);
        }else {
            service.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        service.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/toView")
    public String toView(String id){
        Contract contract = service.findById(id);
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-view";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Contract contract = service.findById(id);
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-update";
    }

    @RequestMapping("/submit")
    public String Submit(String id) throws CustomException{
        Contract contract = service.findById(id);
        if (contract.getState()==0){
            Contract contract1 = new Contract();
            contract1.setId(id);
            contract1.setState(1);
            contract1.setUpdateTime(new Date());
            service.update(contract1);
            return "redirect:/cargo/contract/list.do";
        }else if (contract.getState()==1){
            return "redirect:/cargo/contract/list.do";
        }else {
            throw new CustomException("合同已报运");
        }
    }

    @RequestMapping("/cancel")
    public String cancel(String id) throws CustomException{
        Contract contract = service.findById(id);
        if (contract.getState()==1){
            Contract contract1 = new Contract();
            contract1.setId(id);
            contract1.setState(0);
            contract1.setUpdateTime(new Date());
            service.update(contract1);
            return "redirect:/cargo/contract/list.do";
        }else if (contract.getState()==0){
            return "redirect:/cargo/contract/list.do";
        }else {
            throw new CustomException("合同已报运,无法取消");
        }
    }
}
