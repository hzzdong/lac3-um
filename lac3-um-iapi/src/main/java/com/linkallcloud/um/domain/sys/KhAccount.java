package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("客户账号")
public class KhAccount extends Account {
	private static final long serialVersionUID = 6358395618329617921L;

	public KhAccount() {
		super();
	}

	public KhAccount(String name, String mobile, String loginname, String password, String salt) {
		super(name, mobile, loginname, password, salt);
	}

}
