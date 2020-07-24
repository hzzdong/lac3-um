package com.linkallcloud.um.server.manager.sys;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.iapi.sys.IUmServiceLogManager;
import com.linkallcloud.um.service.sys.IUmServiceLogService;

@Service(interfaceClass = IUmServiceLogManager.class, version = "${dubbo.service.version}")
@Module(name = "服务层日志")
public class UmServiceLogManager extends BaseManager<LacBusiLog, IUmServiceLogService>
		implements IUmServiceLogManager {

	@Autowired
	private IUmServiceLogService lacServiceBusiLogService;

	@Override
	protected IUmServiceLogService service() {
		return lacServiceBusiLogService;
	}

}
