package com.linkallcloud.um.pc.aop;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.security.MsgSignObject;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.web.face.FaceAspect;
import com.linkallcloud.web.face.processor.PackageJsonProcessor;
import com.linkallcloud.web.face.processor.SimpleJsonProcessor;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.session.SimpleSessionUser;

@Aspect
@Component
@Order(2)
public class UmYwFaceAspect extends FaceAspect {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;
	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Pointcut("execution(public * com.linkallcloud.um.pc.face..*.*(..))")
	public void oapiface() {
	}

	@Pointcut("execution(* com.linkallcloud.web.face.base.*.*(..))")
	public void baseface() {
	}

	@Around("oapiface() || baseface()")
	@Override
	public Object checkFace(ProceedingJoinPoint joinPoint) throws Throwable {
		return super.checkFace(joinPoint);
	}

	@Override
	protected PackageJsonProcessor getPackageJsonProcessor() {
		PackageJsonProcessor pjp = new PackageJsonProcessor() {

			@Override
			protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser ssu) {
				return ywUserManager.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
			}

		};
		List<Application> apps = findApplications();
		if (apps != null && !apps.isEmpty()) {
			for (Application app : apps) {
				MsgSignObject mso = new MsgSignObject(app.getCode(), app.getSignatureAlg(), app.getSignatureKey(),
						app.getMessageEncAlg(), app.getMessageEncKey());
				mso.setTimeout(app.getTimeout() < 10 ? 10 : app.getTimeout());
				pjp.addMsgSignObject(app.getCode(), mso);
			}
		}
		return pjp;
	}

	private List<Application> findApplications() {
		Query query = new Query();
		query.addRule(new Equal("status", 0));
		return applicationManager.find(new Trace(true), query);
	}

	@Override
	protected SimpleJsonProcessor getSimpleJsonProcessor() {
		return new SimpleJsonProcessor() {

			@Override
			protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser ssu) {
				return ywUserManager.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
			}

		};
	}

}
