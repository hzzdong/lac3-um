package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class SessionUserRequest extends FaceRequest {
	private static final long serialVersionUID = 9167889761879792147L;

	private String account;
	private String appCode;
	private Long companyId;

	public SessionUserRequest() {
		super();
	}

	public SessionUserRequest(String account, String appCode, Long companyId) {
		this.account = account;
		this.appCode = appCode;
		this.companyId = companyId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public String getAppCode() {
		return appCode;
	}

	@Override
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
