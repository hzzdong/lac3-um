package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.um.service.sys.IAccountService;


public abstract class AccountManager<T extends Account, TS extends IAccountService<T>> extends BaseManager<T, TS>
		implements IAccountManager<T> {

	@Override
	public T fecthByMobile(Trace t, String mobile) {
		return service().fecthByMobile(t, mobile);
	}

	@Override
	public T fecthByAccount(Trace t, String account) {
		return service().fecthByAccount(t, account);
	}

	@Override
	public T loginValidate(Trace t, String accountOrMobile, String password) {
		return service().loginValidate(t, accountOrMobile, password);
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		return service().updatePassword(t, id, uuid, oldPwd, newPwd);
	}

	@Override
	public boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd) {
		return service().modifyPassword(t, account, oldPwd, newPwd);
	}

	@Override
	public T fechByWechatOpenId(Trace t, String openid) {
		return service().fechByWechatOpenId(t, openid);
	}

	@Override
	public boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid) {
		return service().updateAccountWechatOpenId(t, accountId, openid);
	}

//	// @CachePut(value = "openid", key = "#key")
//	@Override
//	public String storeOpenidInCache(Trace t, String key, String openid) {
//		return openid;
//	}
//
//	// @Cacheable(value = "openid", key = "#key")
//	@Override
//	public String fetchOpenidFromCache(Trace t, String key) {
//		return null;
//	}

}
