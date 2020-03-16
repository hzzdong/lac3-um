package com.linkallcloud.um.face.area;

import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.query.rule.QueryRule;

public class ParentIdAreaRequest extends FaceRequest {
	private static final long serialVersionUID = 4565844469558968741L;

	private Long parentId;
	private QueryRule statusRule;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public QueryRule getStatusRule() {
		return statusRule;
	}

	public void setStatusRule(QueryRule statusRule) {
		this.statusRule = statusRule;
	}

}
