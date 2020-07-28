package com.linkallcloud.um.server.manager.sys;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;
import com.linkallcloud.um.service.sys.IUmWebLogService;

@DubboService(interfaceClass = IUmWebLogManager.class, version = "${dubbo.service.version}")
@Module(name = "Web层日志")
public class UmWebLogManager extends BaseManager<LacBusiLog, IUmWebLogService>
		implements IUmWebLogManager {

	@Autowired
	private IUmWebLogService lacWebBusiLogService;

	@Override
	protected IUmWebLogService service() {
		return lacWebBusiLogService;
	}

}
