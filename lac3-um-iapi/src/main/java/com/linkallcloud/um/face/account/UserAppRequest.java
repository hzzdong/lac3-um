package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class UserAppRequest extends FaceRequest {
	private static final long serialVersionUID = -1969833276911235366L;

	private Long userId;
	private Long appId;
	private Long companyId;

	public UserAppRequest(Long userId, Long appId, Long companyId) {
		this.userId = userId;
		this.appId = appId;
		this.companyId = companyId;
	}

	public UserAppRequest() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
