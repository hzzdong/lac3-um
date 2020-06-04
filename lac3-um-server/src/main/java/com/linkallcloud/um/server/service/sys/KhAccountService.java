package com.linkallcloud.um.server.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.um.activity.sys.IKhAccountActivity;
import com.linkallcloud.um.domain.sys.KhAccount;
import com.linkallcloud.um.service.sys.IKhAccountService;

@Service
@Transactional(readOnly = true)
public class KhAccountService extends AccountService<KhAccount, IKhAccountActivity> implements IKhAccountService {
	
	@Autowired
	private IKhAccountActivity khAccountActivity;

	@Override
	public IKhAccountActivity activity() {
		return khAccountActivity;
	}

}
