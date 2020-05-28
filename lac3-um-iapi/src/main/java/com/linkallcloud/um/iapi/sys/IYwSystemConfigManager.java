package com.linkallcloud.um.iapi.sys;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.sys.YwSystemConfig;

public interface IYwSystemConfigManager extends IManager<YwSystemConfig> {

	YwSystemConfig fetch(Trace t, Long companyId, String configItemCode);

	List<YwSystemConfig> find(Trace t, Long companyId);

	Boolean change(Trace t, Sid companyId, String configItemCode, String value);
	
	boolean isEnableOrgPermission(Trace t, Long companyId);
	boolean isEnableAreaPermission(Trace t, Long companyId);
	boolean isEnableManageDepMode(Trace t, Long companyId);
	boolean isEnableZZD(Trace t, Long companyId);
	
}
