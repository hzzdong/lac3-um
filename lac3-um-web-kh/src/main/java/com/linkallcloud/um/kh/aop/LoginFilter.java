package com.linkallcloud.um.kh.aop;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.web.filter.AbstractPrincipalFilter;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.session.SimpleSessionUser;

public class LoginFilter extends AbstractPrincipalFilter {

	private String myAppCode;
	private IKhUserManager khUserManager;

	private String loginUrl;

	public LoginFilter() {
		super();
	}

	public LoginFilter(String myAppCode, IKhUserManager khUserManager, String loginUrl) {
		super();
		this.myAppCode = myAppCode;
		this.khUserManager = khUserManager;
		this.loginUrl = loginUrl;
	}

	public LoginFilter(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	@Override
	protected String getAppCode() {
		return myAppCode;
	}

	@Override
	protected String getLoginUrl() {
		return loginUrl;
	}

	@Override
	protected SessionUser getUserByLoginName(int appClazz, String loginName) {
		SessionUser su = khUserManager.assembleSessionUser(new Trace(true), loginName, myAppCode, null);
		return su;
	}

	@Override
	protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser ssu) {
		return khUserManager.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
	}

}
