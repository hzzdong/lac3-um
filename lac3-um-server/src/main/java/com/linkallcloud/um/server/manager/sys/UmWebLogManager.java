package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.UmWebLog;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;
import com.linkallcloud.um.service.sys.IUmWebLogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = IUmWebLogManager.class, version = "${dubbo.service.version}")
@Module(name = "Web层日志")
public class UmWebLogManager extends BaseManager<UmWebLog, IUmWebLogService>
		implements IUmWebLogManager {

	@Autowired
	private IUmWebLogService lacWebBusiLogService;

	@Override
	protected IUmWebLogService service() {
		return lacWebBusiLogService;
	}

}
