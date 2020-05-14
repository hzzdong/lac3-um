package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.sys.IYwSystemConfigService;

@Service(interfaceClass = IYwSystemConfigManager.class, version = "${dubbo.service.version}")
@Module(name = "系统配置")
public class YwSystemConfigManager extends BaseManager<YwSystemConfig, IYwSystemConfigService>
		implements IYwSystemConfigManager {

	@Autowired
	private IYwSystemConfigService ywSystemConfigService;

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Override
	protected IYwSystemConfigService service() {
		return ywSystemConfigService;
	}

	@Override
	public YwSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		YwSystemConfig config = service().fetch(t, companyId, configItemCode);
		if (config == null) {
			YwCompany company = ywCompanyService.fetchById(t, companyId);
			if (!company.isTopParent()) {
				return this.fetch(t, company.getParentId(), configItemCode);
			}
		}
		return config;
	}

	@Override
	public List<YwSystemConfig> find(Trace t, Long companyId) {
		return service().find(t, companyId);
	}

	@Override
	public Boolean change(Trace t, Sid companyId, String configItemCode, String value) {
		return service().change(t, companyId, configItemCode, value);
	}

}
