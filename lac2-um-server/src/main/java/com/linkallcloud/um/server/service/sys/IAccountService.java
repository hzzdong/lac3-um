package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.dto.Trace;
import com.linkallcloud.exception.BaseRuntimeException;
import com.linkallcloud.service.IService;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountService extends IService<Long, Account> {

    Account fecthByMobile(Trace t, String mobile);

    Account fecthByAccount(Trace t, String account);

    Account loginValidate(Trace t, String accountOrMobile, String password) throws BaseRuntimeException;

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd)
            throws BaseRuntimeException;

    Account fechByWechatOpenId(Trace t, String userType, String openid);

    boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid);

}
