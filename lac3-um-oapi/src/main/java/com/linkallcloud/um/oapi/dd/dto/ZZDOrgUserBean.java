package com.linkallcloud.um.oapi.dd.dto;

import java.io.Serializable;

/**
 * 浙政钉节点和用户的关联
 * @CLASS: ZZDOrgUserBean
 * @DESCRIPTION: 
 * @AUTHOR: miaoj
 * @VERSION: v1.0
 * @DATE: 2019年5月17日
 */
public class ZZDOrgUserBean implements Serializable {
	private static final long serialVersionUID = -1589833184503866006L;
	
	private Long orgNumber;
	private Long userId;

	public Long getOrgNumber() {
		return orgNumber;
	}

	public void setOrgNumber(Long orgNumber) {
		this.orgNumber = orgNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}