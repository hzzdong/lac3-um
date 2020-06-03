package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;
import com.linkallcloud.um.service.sys.IKhSystemConfigService;

@Service(interfaceClass = IKhSystemConfigManager.class, version = "${dubbo.service.version}")
@Module(name = "系统配置")
public class KhSystemConfigManager extends BaseManager<KhSystemConfig, IKhSystemConfigService>
		implements IKhSystemConfigManager {

	@Autowired
	private IKhSystemConfigService khSystemConfigService;

	@Override
	protected IKhSystemConfigService service() {
		return khSystemConfigService;
	}

	@Override
	public KhSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		return service().fetch(t, companyId, configItemCode);
	}

	@Override
	public List<KhSystemConfig> find(Trace t, Long companyId) {
		return service().find(t, companyId);
	}

	@Override
	public Boolean change(Trace t, Sid companyId, String configItemCode, String value) {
		return service().change(t, companyId, configItemCode, value);
	}

	@Override
	public boolean isEnableZZD(Trace t, Long companyId) {
		return service().isEnableZZD(t, companyId);
	}

}
