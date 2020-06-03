package com.linkallcloud.um.iapi.sys;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.sys.KhSystemConfig;

public interface IKhSystemConfigManager extends IManager<KhSystemConfig> {

	KhSystemConfig fetch(Trace t, Long companyId, String configItemCode);

	List<KhSystemConfig> find(Trace t, Long companyId);

	Boolean change(Trace t, Sid companyId, String configItemCode, String value);
	
	boolean isEnableZZD(Trace t, Long companyId);

}
