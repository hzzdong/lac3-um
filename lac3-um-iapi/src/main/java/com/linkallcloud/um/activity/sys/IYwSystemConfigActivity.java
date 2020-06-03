package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.YwSystemConfig;

public interface IYwSystemConfigActivity extends IActivity<YwSystemConfig> {

	YwSystemConfig fetch(Trace t, Long companyId, String configItemCode);
	
	boolean isCustomerManageMode4Area(Trace t, Long companyId);
	boolean isEnableZZD(Trace t, Long companyId);

}
