package com.linkallcloud.um.server.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.um.activity.sys.IYwAccountActivity;
import com.linkallcloud.um.domain.sys.YwAccount;
import com.linkallcloud.um.service.sys.IYwAccountService;

@Service
@Transactional(readOnly = true)
public class YwAccountService extends AccountService<YwAccount, IYwAccountActivity> implements IYwAccountService {

	@Autowired
	private IYwAccountActivity ywAccountActivity;

	@Override
	public IYwAccountActivity activity() {
		return ywAccountActivity;
	}

}
