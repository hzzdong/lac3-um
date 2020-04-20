package com.linkallcloud.um.server.manager.party;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwRoleService;
import com.linkallcloud.um.service.party.IYwUserService;
import com.linkallcloud.um.service.sys.IAreaService;
import com.linkallcloud.web.session.SessionUser;

@Service(interfaceClass = IYwUserManager.class, version = "${dubbo.service.version}")
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
	public Page<YwUser> findPermedUserPage(Trace t, Page<YwUser> page) {
		return service().findPermedUserPage(t, page);
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
	public void cleanOtherUserMobileByUserId(Trace t, String mobile, Long userId) {
		service().cleanOtherUserMobileByUserId(t, mobile, userId);
	}

	@Override
	public SessionUser assembleSessionUser(Trace t, String loginName, String appCode) {
		if (Strings.isBlank(loginName) || Strings.isBlank(appCode)) {
			throw new ArgException("Arg", "loginName和AppCode都不能为空");
		}

		YwUser dbUser = this.fecthByAccount(t, loginName);
		if (dbUser != null) {
			Company dbCompany = companyService().fetchById(t, dbUser.getCompanyId());
			Long parentId = dbCompany.getId();
			String parentUuid = dbCompany.getUuid();
			String orgName = dbCompany.getName();
			if (YwDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
				YwDepartment parent = ywDepartmentService.fetchById(t, dbUser.getParentId());
				if (parent != null) {
					parentId = parent.getId();
					parentUuid = parent.getUuid();
					orgName = parent.getFullName();
				}
			}
			SessionUser su = new SessionUser(dbUser.getId(), dbUser.getUuid(), dbUser.getAccount(), dbUser.getName(),
					dbUser.getUserType(), dbUser.getCompanyId(), dbCompany.getUuid(), dbCompany.getName(), parentId,
					parentUuid, orgName);
			su.setAdmin(dbUser.isAdmin());

			if (dbCompany.getAreaId() != null) {
				Area area = areaService.fetchById(t, dbCompany.getAreaId());
				if (area != null) {
					su.setAreaInfo(area.getId(), area.getUuid(), area.getCode(), area.getName(), area.getLevel());
				}
			}

			Application app = applicationService.fetchByCode(t, appCode);
			String[] perms = this.getUserAppPermissions4Menu(t, su.id(), app.getId());
			su.setPermissions(perms, null, null);
			su.setAppInfo(app.getId(), app.getUuid(), app.getCode(), app.getName());
			return su;
		}
		throw new ArgException("Arg", "Account或AppCode参数错误");
	}

}
