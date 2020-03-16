package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.um.service.sys.IAccountService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = IAccountManager.class, version = "${dubbo.service.version}")
@Module(name = "账号")
public class AccountManager extends BaseManager<Account, IAccountService> implements IAccountManager {

    @Autowired
    private IAccountService accountService;

    @Override
    public Account fecthByMobile(Trace t, String mobile) {
        return service().fecthByMobile(t, mobile);
    }

    @Override
    public Account fecthByAccount(Trace t, String account) {
        return service().fecthByAccount(t, account);
    }

    @Override
    public Account loginValidate(Trace t, String accountOrMobile, String password) {
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
    protected IAccountService service() {
        return accountService;
    }

    @Override
    public Account fechByWechatOpenId(Trace t, String openid) {
        return service().fechByWechatOpenId(t, openid);
    }

    @Override
    public boolean updateAccountWechatOpenId(Trace t, Long accountId, String openid) {
        return service().updateAccountWechatOpenId(t, accountId, openid);
    }

    //@CachePut(value = "openid", key = "#key")
    @Override
    public String storeOpenidInCache(Trace t, String key, String openid) {
        return openid;
    }

    //@Cacheable(value = "openid", key = "#key")
    @Override
    public String fetchOpenidFromCache(Trace t, String key) {
        return null;
    }

}
