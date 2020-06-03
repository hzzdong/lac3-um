package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.sys.IYwSystemConfigActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.sys.IYwSystemConfigDao;

@Component
public class YwSystemConfigActivity extends BaseActivity<YwSystemConfig, IYwSystemConfigDao>
		implements IYwSystemConfigActivity {

	@Autowired
	private IYwSystemConfigDao ywSystemConfigDao;

	@Autowired
	private IYwCompanyDao ywCompanyDao;

	@Override
	public IYwSystemConfigDao dao() {
		return ywSystemConfigDao;
	}

	@Override
	public YwSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		return dao().fetch(t, companyId, configItemCode);
	}

	private YwSystemConfig fetchConfig(Trace t, Long companyId, String configItemCode) {
		YwSystemConfig config = dao().fetch(t, companyId, configItemCode);
		if (config == null) {
			YwCompany company = ywCompanyDao.fetchById(t, companyId);
			if (!company.isTopParent()) {
				Long rooId = company.rootCompanyId();
				return this.fetch(t, rooId, configItemCode);
			}
		}
		return config;
	}

	@Override
	public boolean isCustomerManageMode4Area(Trace t, Long companyId) {
		YwSystemConfig config = fetchConfig(t, companyId, Consts.CONFIG_CUSTOMER_MANAGE_MODE);
		if (config != null) {
			return "area".equals(config.getValue());
		}
		return false;
	}

	@Override
	public boolean isEnableZZD(Trace t, Long companyId) {
		YwSystemConfig config = fetchConfig(t, companyId, Consts.CONFIG_ZZD);
		if (config != null) {
			return "yes".equals(config.getValue());
		}
		return false;
	}
}
