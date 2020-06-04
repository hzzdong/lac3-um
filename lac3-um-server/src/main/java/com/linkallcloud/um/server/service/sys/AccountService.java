package com.linkallcloud.um.server.service.sys;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.sys.IAccountActivity;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.service.sys.IAccountService;

public abstract class AccountService<T extends Account, TA extends IAccountActivity<T>> extends BaseService<T, TA>
		implements IAccountService<T> {

	@Override
	public T fecthByMobile(Trace t, String mobile) {
		return activity().fecthByMobile(t, mobile);
	}

	@Override
	public T fecthByAccount(Trace t, String account) {
		return activity().fecthByAccount(t, account);
	}

	@Override
	@Transactional(readOnly = false)
	public T loginValidate(Trace t, String account, String password) {
		return activity().loginValidate(t, account, password);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		return activity().updatePassword(t, id, uuid, oldPwd, newPwd);
	}

	@Override
	public boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd) {
		T ac = activity().fecthByAccount(t, account);
		if (ac != null) {
			return activity().updatePassword(t, ac.getId(), ac.getUuid(), oldPwd, newPwd);
		}
		return false;
	}

	@Override
	public T fechByWechatOpenId(Trace t, String openid) {
		return activity().fechByWechatOpenId(t, openid);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid) {
		return activity().updateAccountWechatOpenId(t, accountId, openid);
	}

}
