package com.linkallcloud.um.service.sys;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.YwSystemConfig;

public interface IYwSystemConfigService extends IService<YwSystemConfig> {

	YwSystemConfig fetch(Trace t, Long companyId, String configItemCode);

	List<YwSystemConfig> find(Trace t, Long companyId);

	Boolean change(Trace t, Sid companyId, String configItemCode, String value);
	
	boolean isCustomerManageMode4Area(Trace t, Long companyId);
	boolean isEnableZZD(Trace t, Long companyId);

}
