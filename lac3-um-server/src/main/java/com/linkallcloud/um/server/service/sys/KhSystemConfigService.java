package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.sys.IKhSystemConfigActivity;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.service.sys.IKhSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class KhSystemConfigService extends BaseService<KhSystemConfig, IKhSystemConfigActivity>
        implements IKhSystemConfigService {

    @Autowired
    private IKhSystemConfigActivity khSystemConfigActivity;

    @Override
    public KhSystemConfig fetchByCompanyId(Trace t, Long companyId) {
        return activity().fetchByCompanyId(t, companyId);
    }

    @Override
    public IKhSystemConfigActivity activity() {
        return khSystemConfigActivity;
    }

}
