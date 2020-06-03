package com.linkallcloud.um.server.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.sys.IKhSystemConfigActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.constant.KhConfigs;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.service.sys.IKhSystemConfigService;

@Service
@Transactional(readOnly = true)
public class KhSystemConfigService extends BaseService<KhSystemConfig, IKhSystemConfigActivity>
		implements IKhSystemConfigService {

	@Autowired
	private IKhSystemConfigActivity khSystemConfigActivity;

	@Autowired
	private IKhCompanyActivity khCompanyActivity;

	@Override
	public IKhSystemConfigActivity activity() {
		return khSystemConfigActivity;
	}

	@Override
	public KhSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		KhSystemConfig config = activity().fetch(t, companyId, configItemCode);
		if (config == null) {
			KhCompany company = khCompanyActivity.fetchById(t, companyId);
			if (!company.isTopParent()) {
				Long rootId = company.rootCompanyId();
				return this.fetch(t, rootId, configItemCode);
			}
		}
		return config;
	}

	@Override
	public List<KhSystemConfig> find(Trace t, Long companyId) {
		Query query = new Query();
		query.addRule(new Equal("companyId", companyId));
		return super.find(t, query);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean change(Trace t, Sid companyId, String configItemCode, String value) {
		boolean result = true;
		KhSystemConfig dbEntity = activity().fetch(t, companyId.getId(), configItemCode);
		if (dbEntity != null) {
			dbEntity.setValue(value);
			result = activity().update(t, dbEntity);
		} else {
			KhSystemConfig entity = KhConfigs.defaultConfig(t, configItemCode);
			entity.setCompanyId(companyId.getId());
			entity.setValue(value);
			activity().insert(t, entity);
		}
		if (Consts.CONFIG_LOGO.equals(configItemCode)) {
			khCompanyActivity.updateCompanyLogo(t, companyId, value);
		}
		return result;
	}

	@Override
	public boolean isEnableZZD(Trace t, Long companyId) {
		KhSystemConfig config = fetch(t, companyId, Consts.CONFIG_ZZD);
		if (config != null) {
			return "yes".equals(config.getValue());
		}
		return false;
	}

}
