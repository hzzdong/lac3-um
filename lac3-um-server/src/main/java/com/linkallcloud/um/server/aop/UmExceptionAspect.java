package com.linkallcloud.um.server.aop;

import com.linkallcloud.core.exception.BizExceptionAspect;
import com.linkallcloud.um.exception.UmException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-999)
public class UmExceptionAspect extends BizExceptionAspect<UmException> {

    @Pointcut("execution(public * com.linkallcloud.um.server.manager..*.*(..))")
    public void manager() {
    }

    @Pointcut("execution(* com.linkallcloud.core.manager.*.*(..))")
    public void manager2() {
    }

    @Override
    @Around("manager() || manager2()")
    public Object wrapException(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.wrapException(joinPoint);
    }

}
