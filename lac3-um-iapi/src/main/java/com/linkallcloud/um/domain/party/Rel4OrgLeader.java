package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("机构领导")
public class Rel4OrgLeader extends Domain {
	private static final long serialVersionUID = -7896035517824623445L;

	private String orgType;// Company:公司，Department:部门
	private Long orgId;
	private Long userId;

	private String jobPosition;// 职位，参见UM-yw中数据字典设置
	private int sort;// 排序

	public Rel4OrgLeader() {
		super();
	}

	public Rel4OrgLeader(Long orgId, Long userId) {
		super();
		this.orgId = orgId;
		this.userId = userId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
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

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
