package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.um.activity.sys.IAccountActivity;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.exception.AccountException;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.server.dao.sys.IAccountDao;

public abstract class AccountActivity<T extends Account, TD extends IAccountDao<T>> extends BaseActivity<T, TD>
		implements IAccountActivity<T> {

	@Override
	public T fecthByMobile(Trace t, String mobile) {
		return dao().fecthByMobile(t, mobile);
	}

	@Override
	public T fecthByAccount(Trace t, String account) {
		return dao().fecthByAccount(t, account);
	}

	@Override
	public T loginValidate(Trace t, String account, String password) {
		if (Strings.isBlank(account)) {
			throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
		}
		if (Strings.isBlank(password)) {
			throw new AuthException("10002001", "登录名或者密码错误，请重新输入！");
		}

		T dbAccount = dao().fecthByAccount(t, account);
		if (dbAccount == null) {
			throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
		}
		if (dbAccount.getStatus() != 0) {
			throw new AuthException("10002", "您的账号被限制登陆，请联系管理员！");
		}

		// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());

		if (Securities.validePassword4Md5Src(password, dbAccount.getSalt(), dbAccount.getPasswd())) {
			dao().updateLastLoginTime(t, dbAccount.getId());
			return dbAccount;
		}
		throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		T account = this.fetchByIdUuid(t, id, uuid);
		if (account != null) {
			if (Securities.validePassword4Md5Src(oldPwd, account.getSalt(), account.getPasswd())) {
				account.setSalt(account.generateUuid());
				account.setPasswd(Securities.password4Md5Src(newPwd, account.getSalt()));
				int rows = dao().update(t, account);
				if (rows > 0) {
					log.debug("update密码 成功，tid：" + t.getTid() + ", id:" + account.getId());
				} else {
					log.error("update密码 失败，tid：" + t.getTid() + ", id:" + account.getId());
				}
				return retBool(rows);
			}
		}
		return false;
	}

	@Override
	public T fechByWechatOpenId(Trace t, String openid) {
		T account = dao().fechByWechatOpenId(t, openid);
		if (account != null && account.isValid()) {
			return account;
		}
		throw new AccountException("10002", "您的账号暂时无法登陆系统，请联系管理员！");
	}

	@Override
	public boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid) {
//		T account = dao().fechByWechatOpenId(t, openid);
//		if (account != null) {// 已经有绑定，不能再绑定
//			return false;
//		}

		int rows = dao().updateAccountWechatOpenId(t, accountId, openid);
		return retBool(rows);
	}

	@Override
	public boolean updateStatusByCompany(Trace t, int status, Long companyId) {
		int rows = dao().updateStatusByCompany(t, status, companyId);
		return retBool(rows);
	}

	@Override
	public boolean updateStatusByDepartment(Trace t, int status, Long departmentId) {
		int rows = dao().updateStatusByDepartment(t, status, departmentId);
		return retBool(rows);
	}
}
