package com.linkallcloud.um.dto.party;

import com.linkallcloud.um.domain.party.User;

public class UserDto extends User {
	private static final long serialVersionUID = -8946822638605593587L;

	// private String post;//职位，参见UM-yw中数据字典设置

	private String orgName;
	// private String companyName;

	public UserDto() {
		super();
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
