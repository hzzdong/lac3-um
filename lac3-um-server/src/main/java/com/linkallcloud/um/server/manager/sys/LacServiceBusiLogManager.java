package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.um.service.sys.ILacServiceBusiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.Service;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.XfServiceBusiLog;
import com.linkallcloud.um.iapi.sys.ILacServiceBusiLogManager;

@Service(interfaceClass = ILacServiceBusiLogManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "服务层日志")
public class LacServiceBusiLogManager extends BaseManager<XfServiceBusiLog, ILacServiceBusiLogService>
		implements ILacServiceBusiLogManager {

	@Autowired
	private ILacServiceBusiLogService lacServiceBusiLogService;

	@Override
	protected ILacServiceBusiLogService service() {
		return lacServiceBusiLogService;
	}

}
