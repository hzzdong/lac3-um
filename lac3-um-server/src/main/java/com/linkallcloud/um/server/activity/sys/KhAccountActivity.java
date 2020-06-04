package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.um.activity.sys.IKhAccountActivity;
import com.linkallcloud.um.domain.sys.KhAccount;
import com.linkallcloud.um.server.dao.sys.IKhAccountDao;

@Component
public class KhAccountActivity extends AccountActivity<KhAccount, IKhAccountDao> implements IKhAccountActivity {

	@Autowired
	private IKhAccountDao khAccountDao;

	@Override
	public IKhAccountDao dao() {
		return khAccountDao;
	}

}
