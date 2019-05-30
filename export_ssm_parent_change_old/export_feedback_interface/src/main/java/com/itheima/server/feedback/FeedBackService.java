package com.itheima.server.feedback;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.feedback.PeFeedback;

public interface FeedBackService {

    PageInfo<PeFeedback> findAll(int page,int size,String companyId);

    PeFeedback findById(String id);

    void save(PeFeedback peFeedback);

    void update(PeFeedback peFeedback);

    void delete(String id);
}
