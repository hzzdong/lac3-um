package com.linkallcloud.um.server.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.service.sys.IApplicationService;

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
	public Page<Application> findPage4SelfKhCompany(Trace t, Page<Application> page) {
		return activity().findPage4SelfKhCompany(t, page);
	}

	@Override
	public Page<Application> findPage4SelfKhCompany4Select(Trace t, Page<Application> page) {
		if (!page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "khCompanyId,khCompanyUuid参数错误。");
		}
		return activity().findPage4SelfKhCompany4Select(t, page);
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

	@Transactional(readOnly = false)
	@Override
	public Boolean updateMappingInfo(Trace t, Application app) {
		return activity().updateMappingInfo(t, app);
	}

}
