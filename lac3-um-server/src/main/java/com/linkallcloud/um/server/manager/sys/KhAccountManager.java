package com.linkallcloud.um.server.manager.sys;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.sys.KhAccount;
import com.linkallcloud.um.iapi.sys.IKhAccountManager;
import com.linkallcloud.um.service.sys.IKhAccountService;

@Service(interfaceClass = IKhAccountManager.class, version = "${dubbo.service.version}")
@Module(name = "客户账号")
public class KhAccountManager extends AccountManager<KhAccount, IKhAccountService> implements IKhAccountManager {

	@Autowired
	private IKhAccountService khAccountService;

	@Override
	protected IKhAccountService service() {
		return khAccountService;
	}

}
