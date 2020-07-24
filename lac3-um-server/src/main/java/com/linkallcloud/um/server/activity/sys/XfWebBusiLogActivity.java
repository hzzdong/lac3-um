package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.um.activity.sys.IUmWebLogActivity;
import com.linkallcloud.um.server.dao.sys.IXfWebBusiLogDao;

@Component
public class XfWebBusiLogActivity extends BaseActivity<LacBusiLog, IXfWebBusiLogDao> implements IUmWebLogActivity {

    @Autowired
    private IXfWebBusiLogDao xfWebBusiLogDao;

    @Override
    public IXfWebBusiLogDao dao() {
        return xfWebBusiLogDao;
    }
}
