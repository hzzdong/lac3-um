package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.exception.AppException;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.service.sys.IApplicationService;

@DubboService(interfaceClass = IApplicationManager.class, version = "${dubbo.service.version}")
@Module(name = "应用")
public class ApplicationManager extends BaseManager<Application, IApplicationService> implements IApplicationManager {

	@Autowired
	private IApplicationService applicationService;

	@Override
	protected IApplicationService service() {
		return applicationService;
	}

	@Override
	public Page<Application> findPage4YwRole(Trace t, Page<Application> page) throws AppException {
		return service().findPage4YwRole(t, page);
	}

	@Override
	public Page<Application> findPage4YwRole4Select(Trace t, Page<Application> page) {
		return service().findPage4YwRole4Select(t, page);
	}

	@Override
	public Application fetchByCode(Trace t, String code) {
		return service().fetchByCode(t, code);
	}

	@Override
	public Page<Application> findPage4KhRole(Trace t, Page<Application> page) throws AppException {
		return service().findPage4KhRole(t, page);
	}

	@Override
	public Page<Application> findPage4KhRole4Select(Trace t, Page<Application> page) {
		return service().findPage4KhRole4Select(t, page);
	}

	@Override
	public Page<Application> findPage4KhCompany(Trace t, Page<Application> page) throws AppException {
		if (!page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "khCompanyId,khCompanyUuid参数错误。");
		}
		return service().findPage4KhCompany(t, page);
	}

	@Override
	public Page<Application> findPage4SelfKhCompany(Trace t, Page<Application> page) {
		if (!page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "khCompanyId,khCompanyUuid参数错误。");
		}
		return service().findPage4SelfKhCompany(t, page);
	}

	@Override
	public Page<Application> findPage4SelfKhCompany4Select(Trace t, Page<Application> page) {
		return service().findPage4SelfKhCompany4Select(t, page);
	}

	@Override
	public Page<Application> findPage4SelfYwCompany(Trace t, Page<Application> page) {
		return service().findPage4SelfYwCompany(t, page);
	}

	@Override
	public Page<Application> findPage4SelfYwCompany4Select(Trace t, Page<Application> page) {
		return service().findPage4SelfYwCompany4Select(t, page);
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

	@Override
	public Boolean updateIco(Trace t, Sid app, String ico) {
		return service().updateIco(t, app, ico);
	}

}
