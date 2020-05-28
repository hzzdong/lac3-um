package com.linkallcloud.um.server.manager.party;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.party.IKhDepartmentService;
import com.linkallcloud.um.service.party.IKhRoleService;
import com.linkallcloud.um.service.party.IKhUserService;
import com.linkallcloud.um.service.sys.IAreaService;
import com.linkallcloud.um.service.sys.IKhSystemConfigService;
import com.linkallcloud.web.session.SessionUser;

@Service(interfaceClass = IKhUserManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "用户")
public class KhUserManager
		extends UserManager<KhUser, IKhUserService, KhRole, IKhRoleService, KhCompany, IKhCompanyService>
		implements IKhUserManager {

	@Autowired
	private IKhUserService khUserService;

	@Autowired
	private IKhRoleService khRoleService;

	@Autowired
	private IKhCompanyService khCompanyService;

	@Autowired
	private IKhDepartmentService khDepartmentService;

	@Autowired
	private IKhSystemConfigService khSystemConfigService;

	@Autowired
	private IAreaService areaService;

	@Override
	protected IKhUserService service() {
		return khUserService;
	}

	@Override
	protected IKhRoleService roleService() {
		return khRoleService;
	}

	@Override
	protected IKhCompanyService companyService() {
		return khCompanyService;
	}

	@Override
	protected List<Application> findApplicaitons4User(Trace t, Long userId) {
		return applicationService.find4KhUser(t, userId);
	}

	@Override
	public SessionUser assembleSessionUser(Trace t, String loginName, String appCode) {
		if (Strings.isBlank(loginName) || Strings.isBlank(appCode)) {
			throw new ArgException("Arg", "loginName和AppCode都不能为空");
		}

		KhUser dbUser = this.fecthByAccount(t, loginName);
		if (dbUser != null) {
			Company dbCompany = companyService().fetchById(t, dbUser.getCompanyId());
			Long parentId = dbCompany.getId();
			String parentUuid = dbCompany.getUuid();
			String orgName = dbCompany.getName();
			if (KhDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
				KhDepartment parent = khDepartmentService.fetchById(t, dbUser.getParentId());
				if (parent != null) {
					parentId = parent.getId();
					parentUuid = parent.getUuid();
					orgName = parent.getFullName();
				}
			}
			SessionUser su = new SessionUser(dbUser.getId(), dbUser.getUuid(), dbUser.getAccount(), dbUser.getName(),
					dbUser.getUserType(), dbUser.getCompanyId(), dbCompany.getUuid(), dbCompany.getName(), parentId,
					parentUuid, orgName);
			su.setIco(dbUser.getIco());
			su.setAdmin(dbUser.isAdmin());

			if (dbCompany.getAreaId() != null) {
				Area area = areaService.fetchById(t, dbCompany.getAreaId());
				if (area != null) {
					su.setAreaInfo(area.getId(), area.getUuid(), area.getCode(), area.getName(), area.getLevel());
				}
			}

			Application app = applicationService.fetchByCode(t, appCode);
			su.setAppInfo(app.getId(), app.getUuid(), app.getCode(), app.getName());

			String[] perms4Menu = this.getUserAppMenus(t, dbUser.getId(), app.getId());
			Long[] perms4Orgs = getUserAppOrgIds(t, dbUser.getCompanyId(), dbUser.getId(), app.getId());
			Long[] perms4Areas = getUserAppAreaIds(t, dbUser.getCompanyId(), dbUser.getId(), app.getId());
			su.setPermissions(perms4Menu, perms4Orgs, perms4Areas);

			return su;
		}
		throw new ArgException("Arg", "Account或AppCode参数错误");
	}

	private Long[] getUserAppAreaIds(Trace t, Long companyId, Long userId, Long appId) {
		Long[] perms4Areas = new Long[0];
		if (khSystemConfigService.isEnableAreaPermission(t, companyId)) {
			List<Long> ids = service().getUserAppAreas(t, userId, appId);
			if (ids != null && !ids.isEmpty()) {
				perms4Areas = ids.stream().toArray(Long[]::new);
			}
		}
		return perms4Areas;
	}

	private Long[] getUserAppOrgIds(Trace t, Long companyId, Long userId, Long appId) {
		Long[] perms4Orgs = new Long[0];
		if (khSystemConfigService.isEnableOrgPermission(t, companyId)) {
			List<Long> ids = service().getUserAppOrgs(t, userId, appId);
			if (ids != null && !ids.isEmpty()) {
				perms4Orgs = ids.stream().toArray(Long[]::new);
			}
		}
		return perms4Orgs;
	}

	@Override
	public Page<KhUser> page4UnRole4Yw(Trace t, Page<KhUser> page) {
		return service().page4UnRole4Yw(t, page);
	}

	@Override
	public Page<KhUser> page4Role4Yw(Trace t, Page<KhUser> page) {
		return service().page4Role4Yw(t, page);
	}

}
