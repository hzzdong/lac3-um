package com.linkallcloud.um.oapi.dd.dto;

import java.io.Serializable;

/**
 * 节点成员信息（简化）
 * @CLASS: ZZDSubUserBean
 * @DESCRIPTION: 
 * @AUTHOR: miaoj
 * @VERSION: v1.0
 * @DATE: 2019年5月17日
 */
public class ZZDSubUserBean implements Serializable {
	
	private static final long serialVersionUID = 2448672685513178244L;
	
	private Long userId;//人员ID
	private String userName;//姓名
	private String mobile;//手机号
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
