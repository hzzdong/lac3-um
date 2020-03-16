package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("数据字典")
public class Dict extends TreeDomain {
	private static final long serialVersionUID = -7325942889128878205L;

	// private Long typeId;//parentId
	private Long topTypeId;

	public Dict() {
		super();
	}

	public Dict(Long typeId, Long topTypeId, String code, String name) {
		super(typeId, code, name, 0);
		this.topTypeId = topTypeId;
	}

	public Long getTopTypeId() {
		return topTypeId;
	}

	public void setTopTypeId(Long topTypeId) {
		this.topTypeId = topTypeId;
	}

}
