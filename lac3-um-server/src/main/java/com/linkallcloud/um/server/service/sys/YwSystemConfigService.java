package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.sys.IYwSystemConfigActivity;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.service.sys.IYwSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class YwSystemConfigService extends BaseService<YwSystemConfig, IYwSystemConfigActivity>
        implements IYwSystemConfigService {

    @Autowired
    private IYwSystemConfigActivity ywSystemConfigActivity;

    @Override
    public YwSystemConfig fetchByCompanyId(Trace t, Long companyId) {
        return activity().fetchByCompanyId(t, companyId);
    }

    @Override
    public IYwSystemConfigActivity activity() {
        return ywSystemConfigActivity;
    }

}
