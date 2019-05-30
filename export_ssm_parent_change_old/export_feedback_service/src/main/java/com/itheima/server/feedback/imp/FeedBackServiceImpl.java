package com.itheima.server.feedback.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.utils.UtilFuns;
import com.itheima.dao.feedback.PeFeedbackDao;
import com.itheima.domain.feedback.PeFeedback;
import com.itheima.domain.feedback.PeFeedbackExample;
import com.itheima.server.feedback.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    private PeFeedbackDao peFeedbackDao;


    @Override
    public PageInfo<PeFeedback> findAll(int page, int size, String companyId) {
        System.out.println(page);
        System.out.println(size);
        PageHelper.startPage(page,size);
        PeFeedbackExample example = new PeFeedbackExample();
        example.createCriteria().andCompanyIdEqualTo(companyId);
        List<PeFeedback> peFeedbacks = peFeedbackDao.selectByExample(example);
        return new PageInfo<PeFeedback>(peFeedbacks);
    }

    @Override
    public PeFeedback findById(String id) {
        return peFeedbackDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(PeFeedback peFeedback) {
        peFeedback.setFeedbackId(UtilFuns.generateId());
        peFeedback.setState("0");
        peFeedback.setAnswerBy("");
        peFeedback.setSolveMethod("");
        peFeedback.setResolution("");
        peFeedbackDao.insertSelective(peFeedback);
    }

    @Override
    public void update(PeFeedback peFeedback) {
        peFeedbackDao.updateByPrimaryKeySelective(peFeedback);
    }

    @Override
    public void delete(String id) {
        peFeedbackDao.deleteByPrimaryKey(id);
    }
}
