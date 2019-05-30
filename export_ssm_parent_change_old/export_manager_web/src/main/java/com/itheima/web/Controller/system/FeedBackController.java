package com.itheima.web.Controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.feedback.PeFeedback;
import com.itheima.server.feedback.FeedBackService;
import com.itheima.web.Controller.baseClass.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/system/feedback")
public class FeedBackController extends BaseController {

    @Reference
    private FeedBackService feedBackService;

    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        PageInfo<PeFeedback> list = feedBackService.findAll(page, size, user.getCompanyId());
        request.setAttribute("page",list);
        return "system/feedback/feedback-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "system/feedback/feedback-add";
    }

    @RequestMapping("/edit")
    public String edit(PeFeedback peFeedback){
        peFeedback.setCompanyId(companyId);
        peFeedback.setCompanyName(companyName);
        if (peFeedback.getFeedbackId()==null){
            feedBackService.save(peFeedback);
        }else {
            feedBackService.update(peFeedback);
        }
        return "redirect:/system/feedback/list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        feedBackService.delete(id);
        return "redirect:/system/feedback/list.do";
    }

    @RequestMapping("/toView")
    public String toView(String id){
        PeFeedback peFeedback = feedBackService.findById(id);
        request.setAttribute("feedback",peFeedback);
        return "system/feedback/feedback-view";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        PeFeedback peFeedback = feedBackService.findById(id);
        request.setAttribute("feedback",peFeedback);
        return "system/feedback/feedback-update";
    }

    @RequestMapping("/toProcess")
    public String toProcess(String id){
        PeFeedback peFeedback = feedBackService.findById(id);
        request.setAttribute("feedback",peFeedback);
        return "system/feedback/feedback-process";
    }
}
