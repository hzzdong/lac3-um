package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountService extends IService<Account> {

    Account fecthByMobile(Trace t, String mobile);

    Account fecthByAccount(Trace t, String account);

    Account loginValidate(Trace t, String accountOrMobile, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);
    boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd);

    Account fechByWechatOpenId(Trace t, String openid);

    boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid);

}
