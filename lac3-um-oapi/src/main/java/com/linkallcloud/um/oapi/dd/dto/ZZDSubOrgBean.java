package com.linkallcloud.um.oapi.dd.dto;

import java.io.Serializable;

/**
 * 子节点信息（简化）
 * @CLASS: ZZDSubOrgBean
 * @DESCRIPTION: 
 * @AUTHOR: miaoj
 * @VERSION: v1.0
 * @DATE: 2019年5月17日
 */
public class ZZDSubOrgBean implements Serializable {
	
	private static final long serialVersionUID = -7905137373319945152L;
	
	private Long orgNumber;//节点ID
	private String name;//节点名
	public Long getOrgNumber() {
		return orgNumber;
	}
	public void setOrgNumber(Long orgNumber) {
		this.orgNumber = orgNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
