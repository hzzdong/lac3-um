package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.service.BaseBusiLogService;
import com.linkallcloud.um.activity.sys.IUmServiceLogActivity;
import com.linkallcloud.um.domain.sys.UmServiceLog;
import com.linkallcloud.um.service.sys.IUmServiceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UmServiceLogService extends BaseBusiLogService<UmServiceLog, IUmServiceLogActivity>
        implements IUmServiceLogService {

    @Autowired
    private IUmServiceLogActivity xfServiceBusiLogActivity;

    @Override
    public IUmServiceLogActivity activity() {
        return xfServiceBusiLogActivity;
    }

}
