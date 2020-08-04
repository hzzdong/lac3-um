package com.linkallcloud.um.server.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.busilog.BusiServiceLogAspect;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.log.core.rocketmq.RocketmqProducerClient;
import com.linkallcloud.um.service.sys.IUmLogService;

@Aspect
@Component
@Order(5)
public class UmServerLogAspect extends BusiServiceLogAspect<BusiLog> {
	@Autowired
	private IUmLogService umLogService;

	@Value("${log.storage.type:es}")
	private String logStorageType;
	@Value("${log.appName}")
	private String appName;
	@Value("${log.appType}")
	private String appType;

	@Override
	protected void logStorage(BusiLog operatelog) throws Exception {
		if (operatelog != null) {
			operatelog.setAppName(appName);
			operatelog.setAppType(appType);
			if ("es".equals(logStorageType)) {
				BusiLog log = new BusiLog();
				BeanUtils.copyProperties(operatelog, log);
				log.setError(null);
				log.setCreateTime(null);
				log.setUuid(null);
				String logStr = JSON.toJSONString(log);
				RocketmqProducerClient.getInstance().sendMsg(logStr);
			} else {
				if (operatelog.getErrorMessage() != null && operatelog.getErrorMessage().length() > 512) {
					operatelog.setErrorMessage(operatelog.getErrorMessage().substring(0, 512));
				}
				umLogService.insert(new Trace(operatelog.getTid()), operatelog);
			}
		}
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
