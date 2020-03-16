package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.Application;

import java.util.List;

public interface IApplicationService extends IService<Application> {

    Application fetchByCode(Trace t, String code);
    
    Boolean updateInterfaceInfo(Trace t, Application app);
    Boolean updateMappingInfo(Trace t, Application app);

    /**
     * 查询某角色许可的应用
     * 
     * @param t
     * @param page
     *            查询条件中必须包含：roleId和roleUuid参数
     * 
     * @return
     */
    Page<Application> findPage4YwRole(Trace t, Page<Application> page);

    Page<Application> findPage4KhRole(Trace t, Page<Application> page);

    Page<Application> findPage4KhCompany(Trace t, Page<Application> page);
    
    List<Application> find4YwUser(Trace t, Long ywUserId);
    List<Application> find4KhUser(Trace t, Long khUserId);
}
