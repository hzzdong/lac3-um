package com.linkallcloud.um.face.account;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.face.message.request.FaceRequest;

public class SessionUserRequest extends FaceRequest {
	private static final long serialVersionUID = 9167889761879792147L;

	private String account;
	private String appCode;
	private Long orgId;
	private String orgType;

	public SessionUserRequest() {
		super();
	}

	public SessionUserRequest(String account, String appCode, Sid org) {
		this.account = account;
		this.appCode = appCode;
		if (org != null) {
			this.orgId = org.getId();
			this.orgType = org.getCode();
		}
	}

	public SessionUserRequest(String account, String appCode, Long orgId, String orgType) {
		this.account = account;
		this.appCode = appCode;
		this.orgId = orgId;
		this.orgType = orgType;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

}
