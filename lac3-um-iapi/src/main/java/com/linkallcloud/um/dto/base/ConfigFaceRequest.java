package com.linkallcloud.um.dto.base;

import com.linkallcloud.core.face.message.request.LoginFaceRequest;

public class ConfigFaceRequest extends LoginFaceRequest {
	private static final long serialVersionUID = -6045063611556952664L;

	private Long orgId;
	private String orgUuid;

	private String key;
	private String value;

	public ConfigFaceRequest() {
		super();
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgUuid() {
		return orgUuid;
	}

	public void setOrgUuid(String orgUuid) {
		this.orgUuid = orgUuid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
