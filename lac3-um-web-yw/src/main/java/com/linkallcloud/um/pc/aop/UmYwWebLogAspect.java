package com.linkallcloud.um.pc.aop;

import com.linkallcloud.um.domain.sys.UmWebLog;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;
import com.linkallcloud.web.busilog.BusiWebLogAspect;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(5)
public class UmYwWebLogAspect extends BusiWebLogAspect<UmWebLog, IUmWebLogManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IUmWebLogManager umWebLogManager;

    @Override
    protected IUmWebLogManager logService() {
        return umWebLogManager;
    }

    // @Pointcut("@annotation(com.linkallcloud.core.busilog.annotation.WebLog)")
    @Pointcut("execution(public * com.linkallcloud.um.pc.face..*.*(..))")
    public void xfWebLog() {
    }

    @Pointcut("execution(* com.linkallcloud.web.face.base.*.*(..))")
    public void webLog() {
    }

    @Override
    @Around("xfWebLog() || webLog()")
    public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.autoLog(joinPoint);
    }

}
