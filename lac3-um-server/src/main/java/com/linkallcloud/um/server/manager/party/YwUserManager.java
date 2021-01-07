package com.linkallcloud.um.server.manager.party;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.ptj.YwPartTimeJob;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwRoleService;
import com.linkallcloud.um.service.party.IYwUserService;
import com.linkallcloud.um.service.ptj.IYwPartTimeJobService;
import com.linkallcloud.um.service.sys.IAreaService;
import com.linkallcloud.web.session.SessionUser;

@DubboService(interfaceClass = IYwUserManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "用户")
public class YwUserManager
		extends UserManager<YwUser, IYwUserService, YwRole, IYwRoleService, YwCompany, IYwCompanyService>
		implements IYwUserManager {

	@Autowired
	private IYwUserService ywUserService;

	@Autowired
	private IYwRoleService ywRoleService;

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Autowired
	private IYwDepartmentService ywDepartmentService;

	@Autowired
	private IAreaService areaService;

	@Autowired
	private IYwPartTimeJobService ywPartTimeJobService;

	@Override
	protected IYwUserService service() {
		return ywUserService;
	}

	@Override
	protected IYwRoleService roleService() {
		return ywRoleService;
	}

	@Override
	protected IYwCompanyService companyService() {
		return ywCompanyService;
	}

	@Override
	protected List<Application> findApplicaitons4User(Trace t, Long userId) {
		return applicationService.find4YwUser(t, userId);
	}

	// @CacheEvict(value = "YwUser", key = "\"ID-\" + #entity.id")
	@Override
	public boolean update(Trace t, YwUser entity) throws BaseRuntimeException {
		return super.update(t, entity);
	}

	@Override
	public YwUser findByMobileAndDdStatus(Trace t, String mobile) {
		return service().findByMobileAndDdStatus(t, mobile);
	}

	@Override
	public YwUser fetchByUuid(Trace t, String uuid) {
		return service().fetchByUuid(t, uuid);
	}

	@Override
	public void cleanOtherUserMobileByUserId(Trace t, String mobile, Long userId) {
		service().cleanOtherUserMobileByUserId(t, mobile, userId);
	}

	@Override
	public SessionUser assembleSessionUser(Trace t, String loginName, String appCode, Sid loginOrg) {
		if (Strings.isBlank(loginName) || Strings.isBlank(appCode)) {
			throw new ArgException("Arg", "loginName和AppCode都不能为空");
		}

		YwUser dbUser = this.fecthByAccount(t, loginName);
		if (dbUser != null && dbUser.getStatus() == Domain.STATUS_NORMAL) {
			Org selfOrg = getUserSelfOrg(t, dbUser);
			Company company = null;
			Org org = null;
			if (loginOrg != null) {
				if ("YwCompany".equals(loginOrg.getCode())) {
					company = companyService().fetchById(t, loginOrg.getId());
					org = company;
				} else {
					org = ywDepartmentService.fetchById(t, loginOrg.getId());
					company = companyService().fetchById(t, org.myCompanyId());
				}
			} else {
				if (selfOrg instanceof YwCompany) {
					company = (YwCompany) selfOrg;
				} else {
					company = companyService().fetchById(t, dbUser.getCompanyId());
				}
				org = selfOrg;
			}

			SessionUser su = new SessionUser(dbUser.getId(), dbUser.getUuid(), dbUser.getAccount(), dbUser.getName(),
					dbUser.getUserType(), company.getId(), company.getUuid(), company.getClass().getSimpleName(),
					company.getName(), org.getId(), org.getUuid(), org.getClass().getSimpleName(), org.getName());
			su.setIco(dbUser.getIco());
			su.setAdmin(dbUser.isAdmin());

			if (company.getAreaId() != null) {
				Area area = areaService.fetchById(t, company.getAreaId());
				if (area != null) {
					su.setAreaInfo(area.getId(), area.getUuid(), area.getCode(), area.getName(), area.getLevel());
				}
			}

			Application app = applicationService.fetchByCode(t, appCode);
			su.setAppInfo(app.getId(), app.getUuid(), app.getCode(), app.getName());

			String[] perms4Menu = this.getUserAppMenus(t, company.getId(), dbUser.getId(), app.getId());
			Long[] perms4Orgs = getUserAppOrgIds(t, company.getId(), dbUser.getId(), app.getId());
			Long[] perms4Areas = getUserAppAreaIds(t, company.getId(), dbUser.getId(), app.getId());
			su.setPermissions(perms4Menu, perms4Orgs, perms4Areas);

			/* 处理所在机构+兼职机构列表 */
			List<Sid> myOrgs = new ArrayList<>();
			// orgId,companyId,orgClass,orgName
			myOrgs.add(new Sid(selfOrg.getId(), selfOrg.myCompanyId().toString(), selfOrg.getClass().getSimpleName(),
					(selfOrg instanceof YwCompany) ? selfOrg.getName()
							: (company.getName() + "-" + selfOrg.getName())));
			List<YwPartTimeJob> jzOrgs = getUserJzOrgs(t, dbUser.getId());
			if (jzOrgs != null && !jzOrgs.isEmpty()) {
				for (YwPartTimeJob jzOrg : jzOrgs) {
					myOrgs.add(new Sid(jzOrg.getDestOrgId(), jzOrg.getDestCompanyId().toString(),
							jzOrg.getDestOrgClass(), jzOrg.getDestOrgName()));
				}
			}
			su.setMyOrgs(myOrgs);

			return su;
		}
		throw new ArgException("Arg", "您的账号无法登陆，请联系管理员");
	}

	private Org getUserSelfOrg(Trace t, YwUser dbUser) {
		if (YwDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
			return ywDepartmentService.fetchById(t, dbUser.getParentId());
		} else {
			return companyService().fetchById(t, dbUser.getCompanyId());
		}
	}

	private List<YwPartTimeJob> getUserJzOrgs(Trace t, Long userId) {
		Query query = new Query();
		query.addRule(new Equal("userId", userId));
		return ywPartTimeJobService.find(t, query);
	}

	private Long[] getUserAppAreaIds(Trace t, Long companyId, Long userId, Long appId) {
		Long[] perms4Areas = new Long[0];
		List<Long> ids = service().getUserAppAreas(t, companyId, userId, appId);
		if (ids != null && !ids.isEmpty()) {
			perms4Areas = ids.stream().toArray(Long[]::new);
		}
		return perms4Areas;
	}

	private Long[] getUserAppOrgIds(Trace t, Long companyId, Long userId, Long appId) {
		Long[] perms4Orgs = new Long[0];
		List<Long> ids = service().getUserAppOrgs(t, companyId, userId, appId);
		if (ids != null && !ids.isEmpty()) {
			perms4Orgs = ids.stream().toArray(Long[]::new);
		}
		return perms4Orgs;
	}

}
