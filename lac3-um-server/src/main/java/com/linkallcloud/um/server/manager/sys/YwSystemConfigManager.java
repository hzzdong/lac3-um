package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.um.service.sys.IYwSystemConfigService;

@DubboService(interfaceClass = IYwSystemConfigManager.class, version = "${dubbo.service.version}")
@Module(name = "系统配置")
public class YwSystemConfigManager extends BaseManager<YwSystemConfig, IYwSystemConfigService>
		implements IYwSystemConfigManager {

	@Autowired
	private IYwSystemConfigService ywSystemConfigService;

	@Override
	protected IYwSystemConfigService service() {
		return ywSystemConfigService;
	}

	@Override
	public YwSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		return service().fetch(t, companyId, configItemCode);
	}

	@Override
	public List<YwSystemConfig> find(Trace t, Long companyId) {
		return service().find(t, companyId);
	}

	@Override
	public Boolean change(Trace t, Sid companyId, String configItemCode, String value) {
		return service().change(t, companyId, configItemCode, value);
	}

	@Override
	public boolean isCustomerManageMode4Area(Trace t, Long companyId) {
		return service().isCustomerManageMode4Area(t, companyId);
	}

	@Override
	public boolean isEnableZZD(Trace t, Long companyId) {
		return service().isEnableZZD(t, companyId);
	}

}
