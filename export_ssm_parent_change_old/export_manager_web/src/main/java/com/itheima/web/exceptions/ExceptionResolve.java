package com.itheima.web.exceptions;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ExceptionResolve implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        System.out.println(e.getMessage());
        if (e instanceof CustomException){
            modelAndView.addObject("message",e.getMessage());
        }else if (e instanceof UnauthorizedException){
            modelAndView.setViewName("forward:/unauthorized.jsp");
        }else {
            e.printStackTrace();
            modelAndView.addObject("message","服务器正忙，您歇会");
        }
        return modelAndView;
    }
}
