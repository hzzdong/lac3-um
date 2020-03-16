package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.service.BaseBusiLogService;
import com.linkallcloud.um.activity.sys.IUmWebLogActivity;
import com.linkallcloud.um.domain.sys.UmWebLog;
import com.linkallcloud.um.service.sys.IUmWebLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UmWebLogService extends BaseBusiLogService<UmWebLog, IUmWebLogActivity>
        implements IUmWebLogService {

    @Autowired
    private IUmWebLogActivity xfWebBusiLogActivity;

    @Override
    public IUmWebLogActivity activity() {
        return xfWebBusiLogActivity;
    }

}
