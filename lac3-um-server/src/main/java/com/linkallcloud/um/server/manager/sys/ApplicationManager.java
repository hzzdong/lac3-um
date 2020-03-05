package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.exception.AppException;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.service.sys.IApplicationService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = IApplicationManager.class, version = "${dubbo.service.version}")
@Module(name = "应用")
public class ApplicationManager extends BaseManager<Application, IApplicationService>
        implements IApplicationManager {

    @Autowired
    private IApplicationService applicationService;

    @Override
    protected IApplicationService service() {
        return applicationService;
    }

    @Override
    public Page<Application> findPage4YwRole(Trace t, Page<Application> page)
            throws AppException {
        if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
            throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
        }
        return service().findPage4YwRole(t, page);
    }

    @Override
    public Application fetchByCode(Trace t, String code) {
        return service().fetchByCode(t, code);
    }

    @Override
    public Page<Application> findPage4KhRole(Trace t, Page<Application> page)
            throws AppException {
        if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
            throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
        }
        return service().findPage4KhRole(t, page);
    }

    @Override
    public Page<Application> findPage4KhCompany(Trace t, Page<Application> page) throws AppException {
        if (!page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
            throw new AppException(Exceptions.CODE_ERROR_PARAMETER,
                    "khCompanyId,khCompanyUuid参数错误。");
        }
        return service().findPage4KhCompany(t, page);
    }

    @Override
    public List<Application> find4YwUser(Trace t, Long ywUserId) {
        return service().find4YwUser(t, ywUserId);
    }

    @Override
    public List<Application> find4KhUser(Trace t, Long khUserId) {
        return service().find4KhUser(t, khUserId);
    }

    @Override
    public Boolean updateInterfaceInfo(Trace t, Application app) {
        return service().updateInterfaceInfo(t, app);
    }

    @Override
    public Boolean updateMappingInfo(Trace t, Application app) {
        return service().updateMappingInfo(t, app);
    }

}
