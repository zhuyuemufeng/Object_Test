package com.itheima.dao.cargo;



import com.itheima.domain.cargo.ContractProduct;
import com.itheima.domain.cargo.ContractProductExample;

import java.util.List;

public interface ContractProductDao {

	//删除
    int deleteByPrimaryKey(String id);

    //依据合同号删除
    //有个因患！！！
    int deleteByContractProduct(ContractProduct record);

	//保存
    int insertSelective(ContractProduct record);

	//条件查询
    List<ContractProduct> selectByExample(ContractProductExample example);

	//id查询
    ContractProduct selectByPrimaryKey(String id);

	//更新
    int updateByPrimaryKeySelective(ContractProduct record);

}