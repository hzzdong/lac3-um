package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.um.activity.sys.IUmServiceLogActivity;
import com.linkallcloud.um.server.dao.sys.IXfServiceBusiLogDao;

@Component
public class XfServiceBusiLogActivity extends BaseActivity<LacBusiLog, IXfServiceBusiLogDao> implements IUmServiceLogActivity {

    @Autowired
    private IXfServiceBusiLogDao IXfServiceBusiLogDao;

    @Override
    public IXfServiceBusiLogDao dao() {
        return IXfServiceBusiLogDao;
    }
}
