package com.linkallcloud.um.pc.aop;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
@Order(5)
public class UmYwLogAspect extends BusiWebLogAspect<BusiLog> {

	@Value("${log.storage.type:es}")
	private String logStorageType;
	@Value("${log.appName}")
	private String appName;
	@Value("${log.appType}")
	private String appType;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IUmLogManager umLogManager;

	@Override
	protected void logStorage(BusiLog log) throws Exception {
		if (log != null) {
			log.setAppName(appName);
			log.setAppType(appType);
			if ("es".equals(logStorageType)) {
				log.setError(null);
				log.setCreateTime(null);
				log.setUuid(null);
				String logStr = JSON.toJSONString(log);
				RocketmqProducerClient.getInstance().sendMsg(logStr);
			} else {
				umLogManager.insert(new Trace(log.getTid()), log);
			}
		}
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
