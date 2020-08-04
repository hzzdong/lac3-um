package com.linkallcloud.um.server.manager.sys;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.manager.LacLogManager;
import com.linkallcloud.um.iapi.sys.IUmLogManager;
import com.linkallcloud.um.service.sys.IUmLogService;

@DubboService(interfaceClass = IUmLogManager.class, version = "${dubbo.service.version}")
@Module(name = "UM日志")
public class UmLogManager extends LacLogManager<BusiLog, IUmLogService> implements IUmLogManager {

	@Autowired
	private IUmLogService umLogService;

	@Override
	protected IUmLogService service() {
		return umLogService;
	}

}
