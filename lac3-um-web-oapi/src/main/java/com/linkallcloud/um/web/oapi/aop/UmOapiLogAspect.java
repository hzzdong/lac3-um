package com.linkallcloud.um.web.oapi.aop;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.log.core.rocketmq.RocketmqProducerClient;
import com.linkallcloud.um.iapi.sys.IUmLogManager;
import com.linkallcloud.web.busilog.BusiWebLogAspect;

@Aspect
@Component
@Order(3)
public class UmOapiLogAspect extends BusiWebLogAspect<BusiLog> {

    @Value("${log.storage.type:es}")
    private String logStorageType;
    @Value("${log.appName}")
    private String appName;
    @Value("${log.appType}")
    private String appType;

    @DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IUmLogManager umLogManager;

    @Override
    protected void logStorage(BusiLog operatelog) throws Exception {
        if (operatelog != null) {
            operatelog.setAppName(appName);
            operatelog.setAppType(appType);
            if ("es".equals(logStorageType)) {
            	BusiLog log = new BusiLog();
                BeanUtils.copyProperties(operatelog, log);
                log.setError(null);
                String logStr = JSON.toJSONString(log);
                log.setCreateTime(null);
                log.setUuid(null);
                RocketmqProducerClient.getInstance().sendMsg(logStr);
            } else {
                if (operatelog.getErrorMessage() != null && operatelog.getErrorMessage().length() > 512) {
                    operatelog.setErrorMessage(operatelog.getErrorMessage().substring(0, 512));
                }
                umLogManager.insert(new Trace(operatelog.getTid()), operatelog);
            }
        }
    }

    // @Pointcut("@annotation(com.linkallcloud.core.busilog.annotation.WebLog)")
    @Pointcut("execution(public * com.linkallcloud.um.web.oapi.face..*.*(..))")
    public void xfWebLog() {
    }

    @Pointcut("execution(* com.linkallcloud.web.controller.*.*(..))")
    public void webLog() {
    }

    @Override
    @Around("xfWebLog() || webLog()")
    public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.autoLog(joinPoint);
    }

}
