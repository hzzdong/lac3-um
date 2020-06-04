package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountService<T extends Account> extends IService<T> {

    T fecthByMobile(Trace t, String mobile);

    T fecthByAccount(Trace t, String account);

    T loginValidate(Trace t, String accountOrMobile, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);
    boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd);

    T fechByWechatOpenId(Trace t, String openid);

    boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid);

}
