package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.sys.IKhSystemConfigActivity;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.server.dao.sys.IKhSystemConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KhSystemConfigActivity extends BaseActivity<KhSystemConfig, IKhSystemConfigDao> implements IKhSystemConfigActivity {
    @Autowired
    private IKhSystemConfigDao khSystemConfigDao;

    @Override
    public IKhSystemConfigDao dao() {
        return khSystemConfigDao;
    }

    @Override
    public KhSystemConfig fetchByCompanyId(Trace t, Long companyId) {
        return dao().fetchByCompanyId(t, companyId);
    }
}
