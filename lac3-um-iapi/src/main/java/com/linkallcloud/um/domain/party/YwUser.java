package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("运维用户")
public class YwUser extends User {
	private static final long serialVersionUID = 8095014108495944451L;

	public YwUser() {
		super();
	}

	public YwUser(String name, String account, String mobile, String password) {
		super(name, account, mobile, password);
	}

}
