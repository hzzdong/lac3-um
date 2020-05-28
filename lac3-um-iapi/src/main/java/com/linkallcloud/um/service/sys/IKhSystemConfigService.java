package com.linkallcloud.um.service.sys;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.KhSystemConfig;

public interface IKhSystemConfigService extends IService<KhSystemConfig> {

	KhSystemConfig fetch(Trace t, Long companyId, String configItemCode);

	List<KhSystemConfig> find(Trace t, Long companyId);
	
	Boolean change(Trace t, Sid companyId, String configItemCode, String value);
	
	boolean isEnableOrgPermission(Trace t, Long companyId);
	boolean isEnableAreaPermission(Trace t, Long companyId);
	boolean isEnableManageDepMode(Trace t, Long companyId);
	boolean isEnableZZD(Trace t, Long companyId);

}
