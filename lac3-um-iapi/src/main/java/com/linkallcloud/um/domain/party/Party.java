package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.util.Domains;

public abstract class Party extends TreeDomain {
	private static final long serialVersionUID = -3380518626600403369L;

	// private String govCode;// Org:网格（区域）/组织 编码；Dept：部门编号；User：员工工号

	/*
	 * 以下字段为查询字段
	 */
	private String orgName;

	public Party() {
		super();
	}

	public Party(Long id, String uuid) {
		super(id, uuid);
	}

	public Party(Long id) {
		super(id);
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * 根据自己code得到公司的code
	 * 
	 * @return
	 */
	public String myCompanyCode() {
		return Domains.parseMyCompanyCode(this.getCode());
	}

	public Long myCompanyId() {
		return Domains.parseMyCompanyId(this.getCode());
	}
	
	public Long rootCompanyId() {
        return Domains.parseMyRootCompanyId(this.getCode());
    }

}
