package com.linkallcloud.um.server.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.service.BaseBusiLogService;
import com.linkallcloud.um.activity.sys.IUmWebLogActivity;
import com.linkallcloud.um.service.sys.IUmWebLogService;

@Service
@Transactional(readOnly = true)
public class UmWebLogService extends BaseBusiLogService<LacBusiLog, IUmWebLogActivity>
        implements IUmWebLogService {

    @Autowired
    private IUmWebLogActivity xfWebBusiLogActivity;

    @Override
    public IUmWebLogActivity activity() {
        return xfWebBusiLogActivity;
    }

}
