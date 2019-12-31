package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.service.sys.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ApplicationService extends BaseService<Application, IApplicationActivity> implements IApplicationService {

    @Autowired
    private IApplicationActivity applicationActivity;

    @Override
    public IApplicationActivity activity() {
        return applicationActivity;
    }

    @Override
    public Page<Application> findPage4YwRole(Trace t, Page<Application> page) {
        return activity().findPage4YwRole(t, page);
    }

    @Override
    public Application fetchByCode(Trace t, String code) {
        return activity().fetchByCode(t, code);
    }

    @Override
    public Page<Application> findPage4KhRole(Trace t, Page<Application> page) {
        return activity().findPage4KhRole(t, page);
    }

    @Override
    public Page<Application> findPage4KhCompany(Trace t, Page<Application> page) {
        return activity().findPage4KhCompany(t, page);
    }

    @Override
    public List<Application> find4YwUser(Trace t, Long ywUserId) {
        return activity().find4YwUser(t, ywUserId);
    }

    @Override
    public List<Application> find4KhUser(Trace t, Long khUserId) {
        return activity().find4KhUser(t, khUserId);
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean updateInterfaceInfo(Trace t, Application app) {
        return activity().updateInterfaceInfo(t, app);
    }

}
