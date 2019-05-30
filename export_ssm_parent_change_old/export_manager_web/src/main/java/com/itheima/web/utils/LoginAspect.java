package com.itheima.web.utils;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.SysLog;
import com.itheima.domain.system.User;
import com.itheima.server.system.LogServer;
import com.itheima.web.Controller.baseClass.BaseController;
import com.itheima.web.exceptions.CustomException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LoginAspect {

    @Reference
    private LogServer server;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Around("execution(* com.itheima.web.Controller.system.*.*(..))")
    public Object methodAround(ProceedingJoinPoint joinPoint) throws Exception {
        try {
            SysLog sysLog = new SysLog();
            sysLog.setTime(new Date());
            sysLog.setIp(request.getRemoteAddr());
            User user = (User) session.getAttribute("user");
            if (user == null) {
                sysLog.setUserName("未知用户");

            } else {
                sysLog.setUserName(user.getUserName());
                sysLog.setCompanyId(user.getCompanyId());
                sysLog.setCompanyName(user.getCompanyName());
            }
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                Method method = methodSignature.getMethod();
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String name = annotation.name();
                    sysLog.setAction(name);
                    sysLog.setMethod(method.getName());
                    server.save(sysLog);
                }
            }
            Object[] args = joinPoint.getArgs();
            Object proceed = joinPoint.proceed(args);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if (throwable instanceof CustomException){
                CustomException customException = (CustomException)throwable;
                throw customException;
            }else {
                throw new RuntimeException();
            }
        }
    }
}
