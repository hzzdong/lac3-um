package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("运维账号")
public class YwAccount extends Account {
	private static final long serialVersionUID = -864695685605082732L;

	public YwAccount() {
		super();
	}

	public YwAccount(String name, String mobile, String loginname, String password, String salt) {
		super(name, mobile, loginname, password, salt);
	}

}
