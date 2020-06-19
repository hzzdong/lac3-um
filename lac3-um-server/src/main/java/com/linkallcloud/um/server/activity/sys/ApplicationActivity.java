package com.linkallcloud.um.server.activity.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.exception.AppException;
import com.linkallcloud.um.server.dao.party.IKhRoleDao;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwRoleDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;

@Component
public class ApplicationActivity extends BaseActivity<Application, IApplicationDao> implements IApplicationActivity {

	@Autowired
	private IApplicationDao applicationDao;

	@Autowired
	private IYwUserDao ywUserDao;

	@Autowired
	private IYwCompanyDao ywCompanyDao;

	@Autowired
	private IYwRoleDao ywRoleDao;

	@Autowired
	private IKhRoleDao khRoleDao;

	@Override
	public IApplicationDao dao() {
		return applicationDao;
	}

	@Override
	public Page<Application> findPage4YwRole(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4YwRole(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4YwRole4Select(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
		String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());

		YwRole role = ywRoleDao.fetchByIdUuid(t, roleId, roleUuid);
		if (role == null) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		if (role.getType() != Domains.ROLE_SYSTEM) {
			YwCompany company = ywCompanyDao.fetchById(t, role.getCompanyId());
			if (!company.isTopParent()) {
				page.addRule(new Equal("companyId", role.getCompanyId()));
			}
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4YwRole4Select(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Application fetchByCode(Trace t, String code) {
		return dao().fetchByCode(t, code);
	}

	@Override
	public Page<Application> findPage4KhRole(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		page.checkPageParameters();

		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4KhRole(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4KhRole4Select(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
		String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());

		KhRole role = khRoleDao.fetchByIdUuid(t, roleId, roleUuid);
		if (role == null) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		if (role.getType() != Domains.ROLE_SYSTEM) {
			page.addRule(new Equal("companyId", role.getCompanyId()));
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4KhRole4Select(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4KhCompany(Trace t, Page<Application> page) {
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4KhCompany(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4SelfKhCompany(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "khCompanyId,khCompanyUuid参数错误。");
		}
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4KhCompany(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4SelfKhCompany4Select(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "khCompanyId,khCompanyUuid参数错误。");
		}
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4SelfKhCompany4Select(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4SelfYwCompany(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("ywCompanyId") || !page.hasRule4Field("ywCompanyUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "ywCompanyId,ywCompanyUuid参数错误。");
		}

		Equal cidr = (Equal) page.getRule4Field("ywCompanyId");
		Long companyId = (Long) cidr.getValue();
		YwCompany company = ywCompanyDao.fetchById(t, companyId);
		if (company.isTopParent()) {
			page.delRule4Field("ywCompanyId");
			page.delRule4Field("ywCompanyUuid");
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4SelfYwCompany(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<Application> findPage4SelfYwCompany4Select(Trace t, Page<Application> page) {
		if (page == null || !page.hasRule4Field("ywCompanyId") || !page.hasRule4Field("ywCompanyUuid")) {
			throw new AppException(Exceptions.CODE_ERROR_PARAMETER, "ywCompanyId,ywCompanyUuid参数错误。");
		}
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<Application> list = dao().findPage4SelfYwCompany4Select(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<Application>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public List<Application> find4YwUser(Trace t, Long ywUserId) {
		YwUser user = ywUserDao.fetchById(t, ywUserId);
		if (user != null) {
			if (user.getAccount().equals("superadmin")) {
				Query query = new Query();
				query.addRule(new Equal("status", 0));
				return dao().find(t, query);
			} else {
				return dao().find4YwUser(t, ywUserId);
			}
		}
		return null;
	}

	@Override
	public List<Application> find4KhUser(Trace t, Long khUserId) {
		return dao().find4KhUser(t, khUserId);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean updateInterfaceInfo(Trace t, Application app) {
		int rows = dao().updateInterfaceInfo(t, app);
		return rows == 1;
	}

	@Override
	public Boolean updateMappingInfo(Trace t, Application app) {
		int rows = dao().updateMappingInfo(t, app);
		return rows == 1;
	}

	@Override
	public Boolean updateIco(Trace t, Sid app, String ico) {
		int rows = dao().updateIco(t, app, ico);
		return rows == 1;
	}

}
