package com.linkallcloud.um.oapi.request;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class RoleFaceRequest extends FaceRequest {
	private static final long serialVersionUID = 2456600985358652435L;

	private Long id;
	private Long companyId;

	public RoleFaceRequest() {
		super();
	}

	public RoleFaceRequest(Long id, Long companyId) {
		super();
		this.id = id;
		this.companyId = companyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
