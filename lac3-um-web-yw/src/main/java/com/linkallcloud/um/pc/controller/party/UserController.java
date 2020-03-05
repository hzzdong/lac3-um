package com.linkallcloud.um.pc.controller.party;

import java.util.List;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.web.controller.BaseLController4ParentTree;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.MockUser;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.um.iapi.party.IDepartmentManager;
import com.linkallcloud.um.iapi.party.IOrgManager;
import com.linkallcloud.um.iapi.party.IRoleManager;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.core.www.ISessionUser;

public abstract class UserController<T extends User, S extends IUserManager<T>, R extends Role, RS extends IRoleManager<R, T>, P extends Org, PS extends IOrgManager<P>>
		extends BaseLController4ParentTree<T, S, P, PS> {
	protected Log logger = Logs.get();

	protected Mirror<T> mirror;

	@SuppressWarnings("unchecked")
	public UserController() {
		super();
		try {
			mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
		} catch (Throwable e) {
			if (logger.isWarnEnabled()) {
				logger.warn("!!!Fail to get TypeParams for self!", e);
			}
		}
	}

	public Class<T> getDomainClass() {
		return (null == mirror) ? null : mirror.getType();
	}

	// protected abstract S getUserManager();

	protected abstract <CPS extends ICompanyManager<Company>> CPS getCompanyManager();

	protected abstract <DPS extends IDepartmentManager<Department>> DPS getDepartmentManager();

	protected abstract RS getRoleManager();

	@SuppressWarnings("unchecked")
	@Override
	protected PS parentManager(String parentClass) {
		if (!Strings.isBlank(parentClass) && parentClass.endsWith("Company")) {
			return (PS) getCompanyManager();
		} else {
			return (PS) getDepartmentManager();
		}
	}

	@Override
	protected String getMainPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/main";
	}

	@Override
	protected String getEditPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/edit";
	}

	@Override
	protected String getSelectPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/select";
	}

	@Override
	protected Page<T> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
		ISessionUser su = Controllers.getSessionUser();
		Page<T> page = webPage.toPage();
		if (!page.hasRule4Field("companyId")) {
			page.addRule(new Equal("companyId", Long.valueOf(su.getCompanyId())));
		}
		return manager().findPage(t, page);
	}

	@Override
	protected String doAdd4Parent(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
			AppVisitor av) {
		modelMap.put("admin", av.isAdmin());
		List<R> roles = findCompanyRolesByLevel(t, parentId, parentClass, null, null, av);
		modelMap.put("roles", roles);
		return super.doAdd4Parent(prepare, parentId, parentClass, t, modelMap, av);
	}

	@Override
	protected String doEdit4Parent(boolean prepare, Long parentId, String parentClass, Long id, String uuid, Trace t,
			ModelMap modelMap, AppVisitor av) {
		modelMap.put("admin", av.isAdmin());
		List<R> roles = findCompanyRolesByLevel(t, parentId, parentClass, id, uuid, av);
		modelMap.put("roles", roles);
		return super.doEdit4Parent(prepare, parentId, parentClass, id, uuid, t, modelMap, av);
	}

	protected List<R> findCompanyRolesByLevel(Trace t, Long parentId, String parentClass, Long userId, String userUuid,
			AppVisitor av) {
		List<R> roles = null;
		if (parentId != null && !Strings.isBlank(parentClass)) {
			if (parentClass.endsWith("Company")) {
				Company company = (Company) parentManager(parentClass).fetchById(t, parentId);
				if (company != null) {
					roles = getRoleManager().findCompanyRolesByLevel(t, company.getId(), 9);
				}
			} else if (parentClass.endsWith("Department")) {
				Department depart = (Department) parentManager(parentClass).fetchById(t, parentId);
				if (depart != null) {
					roles = getRoleManager().findCompanyRolesByLevel(t, depart.getCompanyId(),
							12 == depart.getType() ? 9 : null);
				}
			}
		} else {
			if (userId != null) {
				boolean isAdminDepartment = false;
				T user = manager().fetchById(t, userId);
				if (user != null) {
					if (user.getParentClass().endsWith("Company")) {
						isAdminDepartment = true;
					} else {
						Department depart = (Department) parentManager(parentClass).fetchById(t, user.getParentId());
						if (depart != null) {
							isAdminDepartment = 12 == depart.getType();
						}
					}
					roles = getRoleManager().findCompanyRolesByLevel(t, user.getCompanyId(),
							isAdminDepartment == true ? 9 : null);
				}
			}
		}

		if (roles != null && !roles.isEmpty() && userId != null) {
			List<R> userRoles = getRoleManager().find4User(t, userId);
			if (userRoles != null && !userRoles.isEmpty()) {
				for (R role : roles) {
					for (R userrole : userRoles) {
						if (role.getId().equals(userrole.getId())) {
							role.setChecked(true);
						}
					}
				}
			}
		}

		return roles;
	}

	@Override
	protected T doGet4Parent(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		T entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = manager().fetchByIdUuid(t, id, uuid);
			parentId = entity.getParentId();
			parentClass = entity.getParentClass();
		} else {
			entity = mirror.born();
			entity.setParentId(parentId);
			entity.setParentClass(parentClass);
		}

		if (parentId != null && !Strings.isBlank(parentClass)) {
			if (parentClass.endsWith("Company")) {
				Company company = (Company) parentManager(parentClass).fetchById(t, parentId);
				if (company != null) {
					entity.setOrgName(company.getName());
					entity.setParentId(-1 * entity.getParentId());
				}
			} else if (parentClass.endsWith("Department")) {
				Department depart = (Department) parentManager(parentClass).fetchById(t, parentId);
				if (depart != null) {
					entity.setOrgName(depart.getName());
				}
			}
		} else {
			Company company = (Company) getCompanyManager().fetchById(t, entity.getCompanyId());
			if (company != null) {
				entity.setOrgName(company.getName());
				entity.setParentId(-1 * company.getId());
				entity.setParentClass(company.getClass().getSimpleName());
			}
		}

		return entity;
	}

	@Override
	protected T doSave(T user, Trace t, AppVisitor av) {
		if (user.getParentId() != null && user.getParentId().longValue() < 0) {
			user.setParentId(-1 * user.getParentId());
		}

		if (user.getParentId() != null && !Strings.isBlank(user.getParentClass())
				&& user.getParentClass().endsWith("Company")) {
			user.setCompanyId(user.getParentId());
		} else {
			ISessionUser su = Controllers.getSessionUser();
			user.setCompanyId(Long.valueOf(su.getCompanyId()));
		}

		if (!av.isAdmin()) {
			user.setType(0);// 非管理员不能修改用户类型
		}

		if (user.getId() != null && user.getId() > 0 && user.getUuid() != null) {
			manager().update(t, user);
		} else {
			Long id = manager().insert(t, user);
			user.setId(id);
		}

		return user;
	}

	@Override
	protected Page<T> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
		Page<T> page = webPage.toPage();
		Equal rR = (Equal) page.getRule4Field("roleId");
		if (rR != null) {
			R role = getRoleManager().fetchById(t, (Long) rR.getValue());
			page.addRule(new Equal("type", role.getType()));
		}

		Equal cR = (Equal) page.getRule4Field("companyId");
		if (cR != null) {
			cR.setValue(Long.valueOf(av.getCompanyId()));
		} else {
			page.addRule(new Equal("companyId", Long.valueOf(av.getCompanyId())));
		}

		ISessionUser su = Controllers.getSessionUser();
		if (su.isAdmin()) {
			return manager().findPage4Select(t, page);
		} else {
			page.addRule(new Equal("appId", Long.parseLong(su.getAppId())));
			page.addRule(new Equal("userId", Long.valueOf(su.getId())));
			return manager().findPermedUserPage4Select(t, page);
		}
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Trace t, ModelMap modelMap) {
		ISessionUser su = Controllers.getSessionUser();
		T user = manager().fetchById(t, Long.parseLong(su.getId()));
		modelMap.put("user", user);
		return "page/party/" + getDomainClass().getSimpleName() + "/profile";
	}

	@RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
	public @ResponseBody Result<Object> saveProfile(@RequestBody T user, Trace t, ModelMap modelMap) {
		ISessionUser su = Controllers.getSessionUser();
		T dbUser = manager().fetchById(t, Long.parseLong(su.getId()));
		dbUser.setPassword(null);
		dbUser.setName(user.getName());
		dbUser.setMobile(user.getMobile());
		dbUser.setPost(user.getPost());
		dbUser.setJob(user.getJob());
		dbUser.setSms(user.getSms());
		if (!Strings.isBlank(user.getPassword())) {
			dbUser.setPassword(user.getPassword());
		}
		Boolean ret = manager().update(t, dbUser);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/changePwd", method = RequestMethod.POST)
	public @ResponseBody Result<Object> changePwd(@RequestBody T user, Trace t, ModelMap modelMap) {
		ISessionUser su = Controllers.getSessionUser();
		T dbUser = manager().fetchById(t, Long.parseLong(su.getId()));
		Boolean ret = false;
		if (dbUser != null && !Strings.isBlank(user.getOldpassword()) && !Strings.isBlank(user.getPassword())
				&& user.getPassword().length() >= 6) {
			ret = manager().updatePassword(t, dbUser.getId(), dbUser.getUuid(), user.getOldpassword(),
					user.getPassword());
			if (ret == true) {
				Controllers.setSessionObject("pwdStrength", "true");
			}
		}
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "密码修改失败，可能是您输入的原密码错误，请重试。");
	}

	@RequestMapping(value = "/changePwd2", method = RequestMethod.POST)
	public @ResponseBody Result<Object> changePwd2(@RequestBody MockUser user, Trace t, ModelMap modelMap) {
		ISessionUser su = Controllers.getSessionUser();
		T dbUser = manager().fetchById(t, Long.parseLong(su.getId()));
		Boolean ret = false;
		if (dbUser != null && !Strings.isBlank(user.getOldpass()) && !Strings.isBlank(user.getPass())) {
			ret = manager().updatePassword(t, dbUser.getId(), dbUser.getUuid(), user.getOldpass(), user.getPass());
			if (ret == true) {
				Controllers.setSessionObject("pwdStrength", "true");
			}
		}
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "密码修改失败，可能是您输入的原密码错误，请重试。");
	}

}
