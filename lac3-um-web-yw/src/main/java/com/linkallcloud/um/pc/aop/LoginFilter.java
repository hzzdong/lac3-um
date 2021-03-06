package com.linkallcloud.um.pc.aop;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.web.filter.AbstractPrincipalFilter;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.session.SimpleSessionUser;

public class LoginFilter extends AbstractPrincipalFilter {

	private String myAppCode;
	private IYwUserManager ywUserManager;

	private String loginUrl;

	public LoginFilter() {
		super();
	}

	public LoginFilter(String myAppCode, IYwUserManager ywUserManager, String loginUrl) {
		super();
		this.myAppCode = myAppCode;
		this.ywUserManager = ywUserManager;
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
		return ywUserManager.assembleSessionUser(new Trace(true), loginName, myAppCode, null);
	}

	@Override
	protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser ssu) {
		return ywUserManager.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
	}

}
