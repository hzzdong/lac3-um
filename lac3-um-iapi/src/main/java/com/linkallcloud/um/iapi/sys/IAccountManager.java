package com.linkallcloud.um.iapi.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountManager<T extends Account> extends IManager<T> {

    T fecthByMobile(Trace t, String mobile);

    T fecthByAccount(Trace t, String account);

    T loginValidate(Trace t, String accountOrMobile, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);
    boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd);

    T fechByWechatOpenId(Trace t, String openid);

    boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid);

//    String storeOpenidInCache(Trace t, String key, String openid);
//    String fetchOpenidFromCache(Trace t, String key);

}
