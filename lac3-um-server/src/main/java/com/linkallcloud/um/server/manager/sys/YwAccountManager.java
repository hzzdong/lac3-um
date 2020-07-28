package com.linkallcloud.um.server.manager.sys;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.sys.YwAccount;
import com.linkallcloud.um.iapi.sys.IYwAccountManager;
import com.linkallcloud.um.service.sys.IYwAccountService;

@DubboService(interfaceClass = IYwAccountManager.class, version = "${dubbo.service.version}")
@Module(name = "运维账号")
public class YwAccountManager extends AccountManager<YwAccount, IYwAccountService> implements IYwAccountManager {

	@Autowired
	private IYwAccountService ywAccountService;

	@Override
	protected IYwAccountService service() {
		return ywAccountService;
	}

}
