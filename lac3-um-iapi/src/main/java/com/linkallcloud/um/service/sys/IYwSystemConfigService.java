package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.YwSystemConfig;

public interface IYwSystemConfigService extends IService<YwSystemConfig> {

    YwSystemConfig fetchByCompanyId(Trace t, Long companyId);

}
