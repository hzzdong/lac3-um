package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.sys.IYwSystemConfigActivity;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.server.dao.sys.IYwSystemConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YwSystemConfigActivity extends BaseActivity<YwSystemConfig, IYwSystemConfigDao> implements IYwSystemConfigActivity {

    @Autowired
    private IYwSystemConfigDao ywSystemConfigDao;

    @Override
    public IYwSystemConfigDao dao() {
        return ywSystemConfigDao;
    }

    @Override
    public YwSystemConfig fetchByCompanyId(Trace t, Long companyId) {
        return dao().fetchByCompanyId(t, companyId);
    }
}
