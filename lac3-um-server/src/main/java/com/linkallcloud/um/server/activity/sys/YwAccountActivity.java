package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.um.activity.sys.IYwAccountActivity;
import com.linkallcloud.um.domain.sys.YwAccount;
import com.linkallcloud.um.server.dao.sys.IYwAccountDao;

@Component
public class YwAccountActivity extends AccountActivity<YwAccount, IYwAccountDao> implements IYwAccountActivity {

	@Autowired
	private IYwAccountDao ywAccountDao;

	@Override
	public IYwAccountDao dao() {
		return ywAccountDao;
	}

}
