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
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.sys.IYwSystemConfigActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.constant.YwConfigs;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.service.sys.IYwSystemConfigService;

@Service
@Transactional(readOnly = true)
public class YwSystemConfigService extends BaseService<YwSystemConfig, IYwSystemConfigActivity>
		implements IYwSystemConfigService {

	@Autowired
	private IYwSystemConfigActivity ywSystemConfigActivity;

	@Autowired
	private IYwCompanyActivity ywCompanyActivity;

	@Override
	public IYwSystemConfigActivity activity() {
		return ywSystemConfigActivity;
	}

	@Override
	public YwSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		return activity().fetch(t, companyId, configItemCode);
	}

	@Override
	public List<YwSystemConfig> find(Trace t, Long companyId) {
		Query query = new Query();
		query.addRule(new Equal("companyId", companyId));
		return super.find(t, query);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean change(Trace t, Sid companyId, String configItemCode, String value) {
		boolean result = true;
		YwSystemConfig dbEntity = fetch(t, companyId.getId(), configItemCode);
		if (dbEntity != null) {
			dbEntity.setValue(value);
			result = activity().update(t, dbEntity);
		} else {
			YwSystemConfig entity = YwConfigs.defaultConfig(t, configItemCode);
			entity.setCompanyId(companyId.getId());
			entity.setValue(value);
			activity().insert(t, entity);
		}
		if (Consts.CONFIG_LOGO.equals(configItemCode)) {
			ywCompanyActivity.updateCompanyLogo(t, companyId, value);
		}

		return result;
	}

}
