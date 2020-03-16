package com.linkallcloud.um.iapi.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.sys.KhSystemConfig;

public interface IKhSystemConfigManager extends IManager<KhSystemConfig> {

    KhSystemConfig fetchByCompanyId(Trace t, Long companyId);

    //Long save(Trace t, KhSystemConfig entity);

}
