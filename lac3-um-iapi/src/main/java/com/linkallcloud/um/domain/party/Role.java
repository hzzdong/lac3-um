package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("角色")
public abstract class Role extends Party {
	private static final long serialVersionUID = 4753075448649352924L;

	private Long companyId;

	/* 非数据库字段，表示是否选中 */
	private Boolean checked;

	public Role() {
		super();
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
