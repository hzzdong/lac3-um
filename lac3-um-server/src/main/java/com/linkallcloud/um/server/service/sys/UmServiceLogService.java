package com.linkallcloud.um.server.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.service.BaseBusiLogService;
import com.linkallcloud.um.activity.sys.IUmServiceLogActivity;
import com.linkallcloud.um.service.sys.IUmServiceLogService;

@Service
@Transactional(readOnly = true)
public class UmServiceLogService extends BaseBusiLogService<LacBusiLog, IUmServiceLogActivity>
        implements IUmServiceLogService {

    @Autowired
    private IUmServiceLogActivity xfServiceBusiLogActivity;

    @Override
    public IUmServiceLogActivity activity() {
        return xfServiceBusiLogActivity;
    }

}
