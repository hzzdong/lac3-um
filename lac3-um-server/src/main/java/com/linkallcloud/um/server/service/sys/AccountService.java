package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.sys.IAccountActivity;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.service.sys.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountService extends BaseService<Account, IAccountActivity> implements IAccountService {

    @Autowired
    private IAccountActivity accountActivity;

    @Override
    public IAccountActivity activity() {
        return accountActivity;
    }

    @Override
    public Account fecthByMobile(Trace t, String mobile) {
        return activity().fecthByMobile(t, mobile);
    }

    @Override
    public Account fecthByAccount(Trace t, String account) {
        return activity().fecthByAccount(t, account);
    }

    @Override
    @Transactional(readOnly = false)
    public Account loginValidate(Trace t, String account, String password) {
        return activity().loginValidate(t, account, password);
    }

    @Transactional(readOnly = false)
    @Override
    public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
        return activity().updatePassword(t, id, uuid, oldPwd, newPwd);
    }

    @Override
    public boolean modifyPassword(Trace t, String account, String oldPwd, String newPwd) {
        Account ac = activity().fecthByAccount(t, account);
        if (ac != null) {
            return activity().updatePassword(t, ac.getId(), ac.getUuid(), oldPwd, newPwd);
        }
        return false;
    }

    @Override
    public Account fechByWechatOpenId(Trace t, String openid) {
        return activity().fechByWechatOpenId(t, openid);
    }

    @Transactional(readOnly = false)
    @Override
    public boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid) {
        return activity().updateAccountWechatOpenId(t, accountId, openid);
    }

}
