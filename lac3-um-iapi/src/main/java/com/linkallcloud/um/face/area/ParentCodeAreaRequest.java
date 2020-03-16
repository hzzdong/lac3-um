package com.linkallcloud.um.face.area;

import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.query.rule.QueryRule;

public class ParentCodeAreaRequest extends FaceRequest {
	private static final long serialVersionUID = -3411920091900954851L;

	private String parentCode;
	private QueryRule statusRule;

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public QueryRule getStatusRule() {
		return statusRule;
	}

	public void setStatusRule(QueryRule statusRule) {
		this.statusRule = statusRule;
	}

}
