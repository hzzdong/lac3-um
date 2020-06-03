package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("客户单位")
public class KhCompany extends Company {
	private static final long serialVersionUID = -6516840348058773569L;

	private Long ywCompanyId;// 所属运维公司id
	private Long createUserId;// 创建者id

	public KhCompany() {
		super();
	}

	public Long getYwCompanyId() {
		return ywCompanyId;
	}

	public void setYwCompanyId(Long ywCompanyId) {
		this.ywCompanyId = ywCompanyId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

}
