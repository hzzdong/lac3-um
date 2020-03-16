package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.Domain;

public class Rel4OrgLeader extends Domain {
	private static final long serialVersionUID = -7896035517824623445L;

	/*-
	 * Domains:
	 * public static final int ORG_DEPARTMENT = 1;// 部门
	 * public static final int ORG_COMPANY = 2;// 公司
	 */
	private int orgType;

	private Long orgId;

	private Long userId;

	public Rel4OrgLeader() {
		super();
	}

	public Rel4OrgLeader(Long orgId, Long userId) {
		super();
		this.orgId = orgId;
		this.userId = userId;
	}

	public int getOrgType() {
		return orgType;
	}

	public void setOrgType(int orgType) {
		this.orgType = orgType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
