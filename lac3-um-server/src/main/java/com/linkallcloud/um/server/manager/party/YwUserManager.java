package com.linkallcloud.um.server.manager.party;

import java.util.List;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.*;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwRoleService;
import com.linkallcloud.um.service.party.IYwUserService;
import com.linkallcloud.um.service.sys.IAreaService;
import com.linkallcloud.web.session.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.Service;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.party.IYwUserManager;

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
			String orgName = dbCompany.getName();
			if (YwDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
				YwDepartment parent = ywDepartmentService.fetchById(t, dbUser.getParentId());
				if (parent != null) {
					orgName = parent.getFullName();
				}
			}
			SessionUser su = new SessionUser(String.valueOf(dbUser.getId()), dbUser.getUuid(), dbUser.getAccount(),
					dbUser.getName(), dbUser.getUserType(), dbUser.getCompanyId().toString(), dbCompany.getName(),
					dbUser.getParentId() != null ? dbUser.getParentId().toString() : dbUser.getCompanyId().toString(),
					orgName, dbUser.getClass().getSimpleName().substring(0, 2));
			su.setAdmin(dbUser.isAdmin());

			if (dbCompany.getAreaId() != null) {
				Area area = areaService.fetchById(t, dbCompany.getAreaId());
				if (area != null) {
					su.setAreaInfo(area.getId(), area.getLevel(), area.getName());
				}
			}

			Application app = applicationService.fetchByCode(t, appCode);
			String[] perms = this.getUserAppPermissions4Menu(t, Long.valueOf(su.getId()), app.getId());
			su.setPermissions(perms, null, null);
			su.setAppInfo(app.getId().toString(), app.getUuid(), app.getName());
			return su;
		}
		throw new ArgException("Arg", "Account或AppCode参数错误");
	}

}
