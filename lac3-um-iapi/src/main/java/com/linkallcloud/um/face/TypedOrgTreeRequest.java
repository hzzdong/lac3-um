package com.linkallcloud.um.face;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class TypedOrgTreeRequest extends FaceRequest {
	private static final long serialVersionUID = -8407027506999673871L;

	private Long companyId;
	private int[] types;

	public TypedOrgTreeRequest() {
		super();
	}

	public TypedOrgTreeRequest(Long companyId, int[] types) {
		super();
		this.companyId = companyId;
		this.types = types;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public int[] getTypes() {
		return types;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

}
