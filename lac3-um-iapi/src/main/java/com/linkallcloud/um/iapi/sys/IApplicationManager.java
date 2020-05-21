package com.linkallcloud.um.iapi.sys;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;

public interface IApplicationManager extends IManager<Application> {

    Application fetchByCode(Trace t, String code);

    Boolean updateInterfaceInfo(Trace t, Application app);

    Boolean updateMappingInfo(Trace t, Application dbApp);

    Page<Application> findPage4YwRole(Trace t, Page<Application> page);
    Page<Application> findPage4YwRole4Select(Trace t, Page<Application> page);

    Page<Application> findPage4KhRole(Trace t, Page<Application> page);
    Page<Application> findPage4KhRole4Select(Trace t, Page<Application> page);

    Page<Application> findPage4KhCompany(Trace t, Page<Application> page);
    Page<Application> findPage4SelfKhCompany(Trace t, Page<Application> page);
    Page<Application> findPage4SelfKhCompany4Select(Trace t, Page<Application> page);
    Page<Application> findPage4SelfYwCompany(Trace t, Page<Application> page);
    Page<Application> findPage4SelfYwCompany4Select(Trace t, Page<Application> page);

    List<Application> find4YwUser(Trace t, Long ywUserId);

    List<Application> find4KhUser(Trace t, Long khUserId);

	Boolean updateIco(Trace t, Sid app, String ico);

}
