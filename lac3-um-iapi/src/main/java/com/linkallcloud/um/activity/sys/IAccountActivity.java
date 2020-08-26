package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountActivity<T extends Account> extends IActivity<T> {

    T fecthByMobile(Trace t, String mobile);

    T fecthByAccount(Trace t, String account);

    T loginValidate(Trace t, String accountOrMobile, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);

    T fechByWechatOpenId(Trace t, String openid);

    boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid);
    
    boolean updateStatusByCompany(Trace t, int status, Long companyId);
	boolean updateStatusByDepartment(Trace t, int status, Long departmentId);
}
