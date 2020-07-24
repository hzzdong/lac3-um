package com.linkallcloud.um.web.oapi.aop;

import org.apache.dubbo.config.annotation.Reference;
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
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.log.core.rocketmq.RocketmqProducerClient;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;
import com.linkallcloud.web.busilog.BusiWebLogAspect;

@Aspect
@Component
@Order(3)
public class LacWebLogAspect extends BusiWebLogAspect<LacBusiLog> {

    @Value("${log.storage.type:es}")
    private String logStorageType;
    @Value("${log.appName}")
    private String appName;
    @Value("${log.appType}")
    private String appType;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IUmWebLogManager umWebLogManager;

    @Override
    protected void logStorage(LacBusiLog operatelog) throws Exception {
        if (operatelog != null) {
            operatelog.setAppName(appName);
            operatelog.setAppType(appType);
            if ("es".equals(logStorageType)) {
                LacBusiLog log = new LacBusiLog();
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
                umWebLogManager.insert(new Trace(operatelog.getTid()), operatelog);
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
