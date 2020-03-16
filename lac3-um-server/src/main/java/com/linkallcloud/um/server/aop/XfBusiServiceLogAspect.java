package com.linkallcloud.um.server.aop;

import com.linkallcloud.core.busilog.BusiServiceLogAspect;
import com.linkallcloud.um.domain.sys.UmServiceLog;
import com.linkallcloud.um.service.sys.IUmServiceLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(5)
public class XfBusiServiceLogAspect extends BusiServiceLogAspect<UmServiceLog, IUmServiceLogService> {

    @Autowired
    private IUmServiceLogService umServiceLogService;

    @Override
    protected IUmServiceLogService logService() {
        return umServiceLogService;
    }

    // @Pointcut("@annotation(com.linkallcloud.core.busilog.annotation.ServLog)")
    @Pointcut("execution(public * com.linkallcloud.um.server.manager..*.*(..))")
    public void manager() {
    }

    @Pointcut("execution(* com.linkallcloud.core.manager.*.*(..))")
    public void manager2() {
    }

    @Override
    @Around("manager() || manager2()")
    public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.autoLog(joinPoint);
    }

}
