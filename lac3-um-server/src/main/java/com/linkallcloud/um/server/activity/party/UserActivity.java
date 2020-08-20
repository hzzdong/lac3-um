package com.linkallcloud.um.server.activity.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.exception.AccountException;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.server.dao.party.ICompanyDao;
import com.linkallcloud.um.server.dao.party.IDepartmentDao;
import com.linkallcloud.um.server.dao.party.IRoleDao;
import com.linkallcloud.um.server.dao.party.IUserDao;
import com.linkallcloud.um.server.dao.sys.IAccountDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;

public abstract class UserActivity<T extends User, UD extends IUserDao<T>, D extends Department, DD extends IDepartmentDao<D>, C extends Company, CD extends ICompanyDao<C>, R extends Role, RD extends IRoleDao<R, T>, A extends Account, AD extends IAccountDao<A>>
		extends PartyActivity<T, UD> implements IUserActivity<T> {

	public UserActivity() {
		super();
	}

	@Autowired
	protected IApplicationDao applicationDao;

	@Autowired
	protected IMenuDao menuDao;

	protected abstract DD getDepartmentDao();

	protected abstract CD getCompanyDao();

	protected abstract RD getRoleDao();

	protected abstract AD getAccountDao();

	@Override
	public List<T> find4Company(Trace t, Long companyId) {
		return dao().find4Company(t, companyId);
	}

	@Override
	public List<T> find4Department(Trace t, Long departmentId) {
		return dao().find4Department(t, departmentId);
	}

	@Override
	public Long insert(Trace t, T entity) {
		Long id = super.insert(t, entity);
		if (entity.getRoleIds() != null && !entity.getRoleIds().isEmpty()) {
			dao().addUserRoles(t, id, entity.getRoleIds(), entity.getCompanyId());
		}
		return id;
	}

	@Override
	public boolean update(Trace t, T entity) {
		boolean ret = super.update(t, entity);
		if (ret && entity.isRoleEnabled()) {
			Long roledCompanyId = entity.getJzCompanyId() != null ? entity.getJzCompanyId() : entity.getCompanyId();
			dao().removeUserAllRoles(t, entity.getId(), roledCompanyId);
			if (entity.getRoleIds() != null && !entity.getRoleIds().isEmpty()) {
				dao().addUserRoles(t, entity.getId(), entity.getRoleIds(), roledCompanyId);
			}
		}
		return ret;
	}

	@Override
	public Page<T> findPermedUserPage4Select(Trace t, Page<T> page) {
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<T> list = dao().findPermedUserPage4Select(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
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
	public List<T> findByMobile(Trace t, String mobile) {
		return dao().findByMobile(t, mobile);
	}

	//
	// @Override
	// public Page findLeaderPage(Trace t, Page page) {
	// if (!page.hasRule4Field("orgId")) {
	// throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER,
	// "orgId参数错误。");
	// }
	//
	// try {
	// PageHelper.startPage(page.getPageNum(), page.getLength());
	// List<T> list = dao().findLeaderPageByOrgId(t, page);
	// if (list instanceof com.github.pagehelper.Page) {
	// page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
	// page.setRecordsFiltered(page.getRecordsTotal());
	// page.addDataAll(list);
	// }
	// return page;
	// } finally {
	// PageHelper.clearPage();
	// }
	// }

	private List<R> findRolesUuidIds(Trace t, Map<String, Long> roleUuidIds) {
		List<Long> ids = Domains.parseIds(roleUuidIds);
		if (ids != null && ids.size() > 0) {
			List<R> entities = getRoleDao().findByIds(t, ids);
			if (entities != null && !entities.isEmpty()) {
				List<R> results = new ArrayList<R>();
				for (R entity : entities) {
					if (entity.getUuid() != null && entity.getId().equals(roleUuidIds.get(entity.getUuid()))) {
						results.add(entity);
					}
				}
				return results;
			}
		}
		return null;
	}

	@Override
	public boolean addUserRoles(Trace t, Long userId, Long companyId, Map<String, Long> roleUuidIds) {
		if (userId != null && companyId != null && roleUuidIds != null && !roleUuidIds.isEmpty()) {
			T user = fetchById(t, userId);
			if (user != null) {
				List<R> checkedEntities = findRolesUuidIds(t, roleUuidIds);
				if (checkedEntities != null && !checkedEntities.isEmpty()
						&& checkedEntities.size() == roleUuidIds.size()) {
					List<Long> roleIds = Domains.parseIds(roleUuidIds);
					return dao().addUserRoles(t, userId, roleIds, companyId);
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeUserRoles(Trace t, Long userId, Long companyId, Map<String, Long> roleUuidIds) {
		if (userId != null && companyId != null && roleUuidIds != null && !roleUuidIds.isEmpty()) {
			T user = fetchById(t, userId);
			if (user != null) {
				List<R> checkedEntities = findRolesUuidIds(t, roleUuidIds);
				if (checkedEntities != null && !checkedEntities.isEmpty()
						&& checkedEntities.size() == roleUuidIds.size()) {
					List<Long> roleIds = Domains.parseIds(roleUuidIds);
					return dao().removeUserRoles(t, userId, roleIds, companyId);
				}
			}
		}
		return false;
	}

	@Override
	public Page<T> findPage4Role(Trace t, Page<T> page) {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
		String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());
		R role = getRoleDao().fetchByIdUuid(t, roleId, roleUuid);
		if (role == null) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Equal r = (Equal) page.getRule4Field("type");
		if (r != null) {
			r.setValue(role.getType());
		} else {
			page.addRule(new Equal("type", role.getType()));
		}

		Equal rl = (Equal) page.getRule4Field("level");
		if (rl != null) {
			rl.setValue(role.getLevel());
		} else {
			page.addRule(new Equal("level", role.getLevel()));
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<T> list = dao().findPage4Role(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
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
	public Page<T> findPage4UnRole(Trace t, Page<T> page) {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
		String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());
		R role = getRoleDao().fetchByIdUuid(t, roleId, roleUuid);
		if (role == null) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Equal r = (Equal) page.getRule4Field("type");
		if (r != null) {
			r.setValue(role.getType());
		} else {
			page.addRule(new Equal("type", role.getType()));
		}

		Equal rl = (Equal) page.getRule4Field("level");
		if (rl != null) {
			rl.setValue(role.getLevel());
		} else {
			page.addRule(new Equal("level", role.getLevel()));
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<T> list = dao().findPage4UnRole(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
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
	public Page<T> findPage4Select(Trace t, Page<T> page) {
		if (page.hasRule4Field("roleId") && page.hasRule4Field("roleUuid")) {
			Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
			String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());
			R role = getRoleDao().fetchByIdUuid(t, roleId, roleUuid);
			if (role == null) {
				throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
			}

			Equal r = (Equal) page.getRule4Field("type");
			if (r != null) {
				r.setValue(role.getType());
			} else {
				page.addRule(new Equal("type", role.getType()));
			}

			Equal rl = (Equal) page.getRule4Field("level");
			if (rl != null) {
				rl.setValue(role.getLevel());
			} else {
				page.addRule(new Equal("level", role.getLevel()));
			}
		}
		return super.findPage4Select(t, page);
	}

//	@Override
//	public List<T> find4Role(Trace t, Long roleId) {
//		return dao().find4RoleById(t, roleId);
//	}
//
//	@Override
//	public List<T> find4Role(Trace t, Long[] roleIds) {
//		return dao().find4RoleByIds(t, roleIds);
//	}
//
//	@Override
//	public List<T> find4Role(Trace t, String roleGovCode) {
//		return dao().find4RoleByGovCode(t, roleGovCode);
//	}
//
//	@Override
//	public List<T> find4Role(Trace t, String[] roleGovCodes) {
//		return dao().find4RoleByGovCodes(t, roleGovCodes);
//	}
//
//	@Override
//	public List<T> findByRoleCompany(Trace t, Long companyId, Long roleId) {
//		return dao().findByRoleCompany(t, companyId, roleId);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long roleId) {
//		return dao().findDepartmentUser4RoleById(t, departmentId, roleId);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long[] roleIds) {
//		return dao().findDepartmentUser4RoleByIds(t, departmentId, roleIds);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String roleGovCode) {
//		return dao().findDepartmentUser4RoleByGovCode(t, departmentId, roleGovCode);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String[] roleGovCodes) {
//		return dao().findDepartmentUser4RoleByGovCodes(t, departmentId, roleGovCodes);
//	}

	@Override
	public List<Long> getUserAppOrgs(Trace t, Long companyId, Long userId, Long appId) {
		return dao().findUserAppOrgs(t, companyId, userId, appId);
	}

	@Override
	public List<Long> getUserAppAreas(Trace t, Long companyId, Long userId, Long appId) {
		T user = dao().fetchById(t, userId);
		Application app = applicationDao.fetchById(t, appId);
		if (user == null || app == null) {
			return null;
		}
		return dao().findUserAppAreas(t, companyId, userId, appId);
	}

	@Override
	public String[] getUserAppMenus(Trace t, Long companyId, Long userId, Long appId) {
		T user = dao().fetchById(t, userId);
		Application app = applicationDao.fetchById(t, appId);
		if (user == null || app == null) {
			return new String[0];
		}

		if (user.getAccount().equals("superadmin")) {
			List<Menu> menus = menuDao.findAppMenusWithButton(t, appId, true);
			return menu2RescodeArray(menus);
		} else if (user.isAdmin()) {
			List<Menu> menus = findCompanyAppMenusWithButton(t, user.getCompanyId(), appId);
			return menu2RescodeArray(menus);
		} else {
			return dao().findUserAppMenuResCodes(t, companyId, userId, appId);
		}
	}

	protected abstract List<Menu> findCompanyAppMenusWithButton(Trace t, Long companyId, Long appId);

	protected String[] menu2RescodeArray(List<Menu> menus) {
		if (menus != null && !menus.isEmpty()) {
			String[] resCodes = new String[menus.size()];
			int i = 0;
			for (Menu menu : menus) {
				resCodes[i++] = menu.getGovCode();
			}
			return resCodes;
		}
		return new String[0];
	}

	@Override
	public T fecthByAccount(Trace t, String account) {
		return dao().fecthByAccount(t, account);
	}

	@Override
	public Boolean updateHeaderImage(Trace t, Sid user, String ico) {
		int rows = dao().updateHeaderImage(t, user, ico);
		return retBool(rows);
	}

	@Override
	public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd) {
		T user = this.fetchByIdUuid(t, userId, userUuid);
		A account = getAccountDao().fecthByAccount(t, user.getAccount());
		if (account != null) {
			if (Securities.validePassword4Md5Src(oldPwd, account.getSalt(), account.getPasswd())) {
				account.setSalt(account.generateUuid());
				account.setPasswd(Securities.password4Md5Src(newPwd, account.getSalt()));
				int rows = getAccountDao().update(t, account);
				return retBool(rows);
			}
		}
		return false;
	}

	@Override
	public T loginValidate(Trace t, String accountOrMobile, String password) {
		if (Strings.isBlank(accountOrMobile)) {
			throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
		}
		if (Strings.isBlank(password)) {
			throw new AuthException("10002001", "登录名或者密码错误，请重新输入！");
		}

		T dbUser = dao().fecthByAccount(t, accountOrMobile);
		if (dbUser == null) {
			if (accountOrMobile.length() == 11 && StringUtils.isNumeric(accountOrMobile)) {
				List<T> phoneUsers = dao().findByMobile(t, accountOrMobile);
				if (phoneUsers == null || phoneUsers.size() > 1) {
					throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
				}
				dbUser = phoneUsers.get(0);
			} else {
				throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
			}
		}

		// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());

		if (Securities.validePassword4Md5Src(password, dbUser.getSalt(), dbUser.getPassword())) {
			dao().updateLastLoginTime(t, dbUser.getId());
			return dbUser;
		}
		throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
	}

	@Override
	protected void treeBefore(Trace t, boolean isNew, T entity) {
		if (exist(t, entity, "account", entity.getAccount())) {
			throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "已经存在相同账号的用户！");
		}

		if (!Strings.isBlank(entity.getMobile())) {
			if (exist(t, entity, "mobile", entity.getMobile())) {
				throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "已经存在相同手机号码的用户！");
			}
		}

		if (isNew) {// 新增
			if (existAccount(t, entity.getAccount())) {// 统一用户是否存在
				throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "已经存在相同的统一用户账号！");
			}

			entity.setSalt(entity.generateUuid());
			if (Strings.isBlank(entity.getPassword())) {
				throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "密码不能为空！");
			}
			entity.setPassword(Securities.password4Md5Src(entity.getPassword(), entity.getSalt()));
		} else {
			if (!Strings.isBlank(entity.getPassword())) {
				entity.setSalt(entity.generateUuid());
				entity.setPassword(Securities.password4Md5Src(entity.getPassword(), entity.getSalt()));
			}

			updateCodeIfModifiedParent(t, entity);
		}
	}

	protected boolean existAccount(Trace t, String account) {
		Query query = new Query();
		query.addRule(new Equal("loginname", account));
		List<A> accounts = getAccountDao().find(t, query);
		if (accounts != null && !accounts.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	protected void updateCodeIfModifiedParent(Trace t, T user) {
		boolean parentChanged = false;
		T dbEntity = dao().fetchById(t, user.getId());
		if (dbEntity.isTopParent()) {
			if (!user.isTopParent()) {
				parentChanged = true;
			}
		} else {
			if (!dbEntity.getParentId().equals(user.getParentId())) {
				parentChanged = true;
			}
		}

		if (parentChanged) {
			parseUserCode(t, user);
			updateCode(t, user.getId(), user.getCode());
		}
	}

	@Override
	protected void treeAfter(Trace t, boolean isNew, T entity) {
		if (isNew) {// 新增
			parseUserCode(t, entity);
			updateCode(t, entity.getId(), entity.getCode());

			// 自动创建用户登录账号
			autoCreateAccount(t, entity);
		} else {
			if (!Strings.isBlank(entity.getPassword())) {
				A account = getAccountDao().fecthByAccount(t, entity.getAccount());
				if (account != null) {
					account.setPasswd(entity.getPassword());
					account.setSalt(entity.getSalt());
					getAccountDao().update(t, account);
				}
			}
		}

		// 自动创建用户相关其它账户，比如积分账户.若修改的时候发现没有创建，也会进行创建。
		autoCreateOtherAccount4User(t, entity);
	}

	protected abstract void autoCreateAccount(Trace t, T entity);

	protected void autoCreateOtherAccount4User(Trace t, T entity) {

	}

	private void parseUserCode(Trace t, T entity) {
		if (entity.getParentId() == null || Strings.isBlank(entity.getParentClass())) {
			throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "parentId或者parentClass参数错误。");
		}

		if (entity.getParentClass().endsWith("Company")) {
			C company = getCompanyDao().fetchById(t, entity.getCompanyId());
			if (company == null) {
				throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "companyId参数错误。");
			}
			entity.setCode(Domains.genDomainCode(company, entity));
		} else {
			D department = getDepartmentDao().fetchById(t, entity.getParentId());
			if (department != null) {
				entity.setCode(Domains.genDomainCode(department, entity));
			} else {
				throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
			}
		}
	}

	@Override
	public T fecthByAZwuid(Trace t, String zwuid) {
		return dao().fecthByAZwuid(t, zwuid);
	}

	@Override
	public T fecthByDduid(Trace t, String dduid) {
		return dao().fecthByDduid(t, dduid);
	}

	@Override
	public boolean updateZwuid(Trace t, Long umUserId, String zwuid) {
		int rows = dao().updateZwuid(t, umUserId, zwuid);
		if (rows > 0) {
			log.debug("update 成功，tid：" + t.getTid() + ", id:" + umUserId);
		} else {
			log.error("update 失败，tid：" + t.getTid() + ", id:" + umUserId);
		}
		return retBool(rows);
	}

	@Override
	public boolean updateDduid(Trace t, Long umUserId, String dduid, int ddStatus) {
		int rows = dao().updateDduid(t, umUserId, dduid, ddStatus);
		if (rows > 0) {
			log.debug("update 成功，tid：" + t.getTid() + ", id:" + umUserId);
		} else {
			log.error("update 失败，tid：" + t.getTid() + ", id:" + umUserId);
		}
		return retBool(rows);
	}

	@Override
	public boolean updateStatusByCompany(Trace t, int status, Long companyId) {
		int rows = dao().updateStatusByCompany(t, status, companyId);
		if (rows > 0) {
			log.debug("updateStatusByCompany 成功，tid：" + t.getTid() + ", companyId:" + companyId);
		} else {
			log.error("updateStatusByCompany 失败，tid：" + t.getTid() + ", companyId:" + companyId);
		}
		return retBool(rows);
	}

	@Override
	public boolean updateStatusByDepartment(Trace t, int status, Long departmentId) {
		int rows = dao().updateStatusByDepartment(t, status, departmentId);
		if (rows > 0) {
			log.debug("updateStatusByDepartment 成功，tid：" + t.getTid() + ", companyId:" + departmentId);
		} else {
			log.error("updateStatusByDepartment 失败，tid：" + t.getTid() + ", companyId:" + departmentId);
		}
		return retBool(rows);
	}

	@Override
	public boolean isUserDepartmentAdmin(Trace t, Long userId, Long companyId) {
		boolean depAdmin = false;
		List<R> roles = getRoleDao().find4User(t, userId, companyId);
		for (R role : roles) {
			if (role.getGovCode().equals(departmentAdminRoleCode())) {
				depAdmin = true;
				break;
			}
		}
		return depAdmin;
	}

	/**
	 * 部门管理员系统角色编号
	 * 
	 * @return
	 */
	protected abstract String departmentAdminRoleCode();

	@Override
	public T fetchCompanyAdmin(Trace t, Sid companyId) {
		List<T> users = dao().findCompanyAdmin(t, companyId);
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	@Override
	public Page<T> leaderPage(Trace t, Page<T> page) {
		if (!page.hasRule4Field("orgId")) {
			throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "orgId参数错误。");
		}

		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<T> list = dao().leaderPage(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<T> findUserPage4Org(Trace t, Page<T> page) {
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<T> list = dao().findUserPage4Org(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
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
	public Page<T> findPermedUserPage(Trace t, Page<T> page) {
		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<T> list = dao().findPermedUserPage(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

}
