package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.um.activity.sys.IUmServiceLogActivity;
import com.linkallcloud.um.domain.sys.UmServiceLog;
import com.linkallcloud.um.server.dao.sys.IXfServiceBusiLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XfServiceBusiLogActivity extends BaseActivity<UmServiceLog, IXfServiceBusiLogDao> implements IUmServiceLogActivity {

    @Autowired
    private IXfServiceBusiLogDao IXfServiceBusiLogDao;

    @Override
    public IXfServiceBusiLogDao dao() {
        return IXfServiceBusiLogDao;
    }
}
