package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.KhSystemConfig;

public interface IKhSystemConfigActivity extends IActivity<KhSystemConfig> {

    KhSystemConfig fetchByCompanyId(Trace t, Long companyId);

}
