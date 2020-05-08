package com.linkallcloud.um.server.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.sys.IKhSystemConfigActivity;
import com.linkallcloud.um.constant.Consts;
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
		return activity().fetch(t, companyId, configItemCode);
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
		KhSystemConfig dbEntity = fetch(t, companyId.getId(), configItemCode);
		if (dbEntity != null) {
			dbEntity.setValue(value);
			result = activity().update(t, dbEntity);
		} else {
			KhSystemConfig entity = defaultConfig(t, configItemCode);
			entity.setCompanyId(companyId.getId());
			entity.setValue(value);
			activity().insert(t, entity);
		}
		khCompanyActivity.updateCompanyLogo(t, companyId, value);
		return result;
	}

	private KhSystemConfig defaultConfig(Trace t, String key) {
		if (!Strings.isBlank(key)) {
			if (Consts.CONFIG_KH_PERMISSION_ORG.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_KH_PERMISSION_ORG, "启用机构权限", "no", "是否启用机构权限功能");
			} else if (Consts.CONFIG_KH_PERMISSION_AREA.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_KH_PERMISSION_AREA, "启用区域权限", "no", "是否启用区域权限功能");
			} else if (Consts.CONFIG_KH_AREAS.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_KH_AREAS, "根区域", "", "可设置多个区域节点作为根区域");
			} else if (Consts.CONFIG_KH_LOGO.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_KH_LOGO, "LOGO", "", "设置公司LOGO");
			}
		}
		return null;
	}

}
