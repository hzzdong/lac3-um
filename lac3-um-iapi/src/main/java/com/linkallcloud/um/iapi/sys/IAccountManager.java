package com.linkallcloud.um.iapi.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountManager extends IManager<Account> {

    Account fecthByMobile(Trace t, String mobile);

    Account fecthByAccount(Trace t, String account);

    Account loginValidate(Trace t, String accountOrMobile, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);
    boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd);

    Account fechByWechatOpenId(Trace t, String openid);

    boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid);

    String storeOpenidInCache(Trace t, String key, String openid);

    String fetchOpenidFromCache(Trace t, String key);

}
