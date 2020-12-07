package com.linkallcloud.um.server.manager.sys;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.Operation;
import com.linkallcloud.um.iapi.sys.IOperationManager;
import com.linkallcloud.um.service.sys.IOperationService;

@DubboService(interfaceClass = IOperationManager.class, version = "${dubbo.service.version}")
@Module(name = "操作")
public class OperationManager extends BaseManager<Operation, IOperationService> implements IOperationManager {

	@Autowired
	private IOperationService operationService;

	@Override
	public List<Operation> findByMenuId(Trace t, Long menuId) {
		return service().findByMenuId(t, menuId);
	}

	@Override
	public List<Operation> findByAppId(Trace t, Long appId) {
		return service().findByAppId(t, appId);
	}

	@Override
	protected IOperationService service() {
		return operationService;
	}

	@Override
	public Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
		return service().loadAppUriRescodeMap(t, appId);
	}

}
