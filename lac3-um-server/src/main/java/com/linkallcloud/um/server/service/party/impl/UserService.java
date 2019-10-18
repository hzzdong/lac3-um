package com.linkallcloud.um.server.service.party.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.ServLog;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.server.dao.party.ICompanyDao;
import com.linkallcloud.um.server.dao.party.IDepartmentDao;
import com.linkallcloud.um.server.dao.party.IRoleDao;
import com.linkallcloud.um.server.dao.party.IUserDao;
import com.linkallcloud.um.server.dao.sys.IAccountDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;
import com.linkallcloud.um.server.service.party.IUserService;
import com.linkallcloud.core.util.Domains;

@Module(name = "用户")
@Transactional(readOnly = true)
public abstract class UserService<T extends User, M extends IUserDao<T>, D extends Department, DM extends IDepartmentDao<D>, C extends Company, CM extends ICompanyDao<C>, R extends Role, RS extends IRoleDao<R, T>>
		extends PartyService<T, M> implements IUserService<T> {

	@Autowired
	protected IAccountDao accountDao;

	@Autowired
	protected IApplicationDao applicationDao;

	@Autowired
	protected IMenuDao menuDao;

	protected abstract DM getDepartmentDao();

	protected abstract CM getCompanyDao();

	protected abstract RS getRoleDao();

	public UserService() {
		super();
	}

	@Override
	public List<T> find4Company(Trace t, Long companyId) {
		return dao().find4Company(t, companyId);
	}

	@Override
	public List<T> find4Department(Trace t, Long departmentId) {
		return dao().find4Department(t, departmentId);
	}

	@Transactional(readOnly = false)
	@ServLog(db = true)
	public Long insert(Trace t, T entity) throws BaseRuntimeException {
		Long id = super.insert(t, entity);
		if (entity.getRoleIds() != null && !entity.getRoleIds().isEmpty()) {
			dao().addUserRoles(t, id, entity.getRoleIds());
		}
		return id;
	}

	@Transactional(readOnly = false)
	@Override
	public boolean update(Trace t, T entity) throws BaseRuntimeException {
		boolean ret = super.update(t, entity);
		if (ret) {
			dao().removeUserAllRoles(t, entity.getId());
			if (entity.getRoleIds() != null && !entity.getRoleIds().isEmpty()) {
				dao().addUserRoles(t, entity.getId(), entity.getRoleIds());
			}
		}
		return ret;
	}

	@Override
	public Page<Long, T> findPermedUserPage4Select(Trace t, Page<Long, T> page) {
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

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为用户([(${userId})]) 添加关联角色([(${roleUuidIds})]), TID:[(${tid})]")
	public boolean addUserRoles(Trace t, Long userId, String userUuid, Map<String, Long> roleUuidIds) {
		if (userId != null && !Strings.isBlank(userUuid) && roleUuidIds != null && !roleUuidIds.isEmpty()) {
			T user = fetchByIdUuid(t, userId, userUuid);
			if (user != null) {
				List<R> checkedEntities = findRolesUuidIds(t, roleUuidIds);
				if (checkedEntities != null && !checkedEntities.isEmpty()
						&& checkedEntities.size() == roleUuidIds.size()) {
					List<Long> roleIds = Domains.parseIds(roleUuidIds);
					return dao().addUserRoles(t, userId, roleIds);
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为用户([(${userId})]) 移除关联角色([(${roleUuidIds})]), TID:[(${tid})]")
	public boolean removeUserRoles(Trace t, Long userId, String userUuid, Map<String, Long> roleUuidIds) {
		if (userId != null && !Strings.isBlank(userUuid) && roleUuidIds != null && !roleUuidIds.isEmpty()) {
			T user = fetchByIdUuid(t, userId, userUuid);
			if (user != null) {
				List<R> checkedEntities = findRolesUuidIds(t, roleUuidIds);
				if (checkedEntities != null && !checkedEntities.isEmpty()
						&& checkedEntities.size() == roleUuidIds.size()) {
					List<Long> roleIds = Domains.parseIds(roleUuidIds);
					return dao().removeUserRoles(t, userId, roleIds);
				}
			}
		}
		return false;
	}

	@Override
	public Page<Long, T> findPage4Role(Trace t, Page<Long, T> page) throws IllegalParameterException {
		if (page == null || !page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
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
	public List<T> find4Role(Trace t, Long roleId) {
		return dao().find4RoleById(t, roleId);
	}

	@Override
	public List<T> find4Role(Trace t, Long[] roleIds) {
		return dao().find4RoleByIds(t, roleIds);
	}

	@Override
	public List<T> find4Role(Trace t, String roleGovCode) {
		return dao().find4RoleByGovCode(t, roleGovCode);
	}

	@Override
	public List<T> find4Role(Trace t, String[] roleGovCodes) {
		return dao().find4RoleByGovCodes(t, roleGovCodes);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long roleId) {
		return dao().findDepartmentUser4RoleById(t, departmentId, roleId);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long[] roleIds) {
		return dao().findDepartmentUser4RoleByIds(t, departmentId, roleIds);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String roleGovCode) {
		return dao().findDepartmentUser4RoleByGovCode(t, departmentId, roleGovCode);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String[] roleGovCodes) {
		return dao().findDepartmentUser4RoleByGovCodes(t, departmentId, roleGovCodes);
	}

	@Override
	public List<Long> findUserAppPermedOrgs(Trace t, Long userId, Long appId) {
		T user = dao().fetchById(t, userId);
		if (user == null) {
			return null;
		}
		return dao().findUserAppPermedOrgs(t, userId, appId);
	}

	// @Cacheable(value = "Permissions4AppMenu", key = "#userId + \"-\" + #appId")
	@Override
	public String getUserAppPermissions4MenuJsonString(Trace t, Long userId, Long appId) {
		String[] result = getUserAppPermissions4Menu(t, userId, appId);
		return JSON.toJSONString(result);
	}

	@Override
	public String[] getUserAppPermissions4Menu(Trace t, Long userId, Long appId) {
		T user = dao().fetchById(t, userId);
		Application app = applicationDao.fetchById(t, appId);
		if (user == null || app == null) {
			return new String[0];
		}

		if (user.getAccount().equals("superadmin")) {
			List<Menu> menus = menuDao.findAppMenusWithButton(t, appId, true);
			return menu2RescodeArray(menus);
		} else if (user.isAdmin()) {// && ("lac_app_um".equals(app.getCode()) ||
									// "lac_app_um_kh".equals(app.getCode()))
			List<Menu> menus = null;
			if (user.getUserType().equals(KhUser.class.getSimpleName())) {
				menus = menuDao.findKhCompanyAppMenusWithButton(t, user.getCompanyId(), appId, true);
			} else if (user.getUserType().equals(YwUser.class.getSimpleName())) {
				if ("lac_app_um".equals(app.getCode()) || "lac_app_um_kh".equals(app.getCode())) {
					menus = menuDao.findYwCompanyAppMenusWithButton(t, user.getCompanyId(), appId, true);
				} else {
					return getCommonUserAppPermissions4Menu(t, userId, appId);
				}
			}
			return menu2RescodeArray(menus);
		} else {
			return getCommonUserAppPermissions4Menu(t, userId, appId);
		}
	}

	/**
	 * 查询普通用户的应用权限
	 * 
	 * @param t
	 * @param userId
	 * @param appId
	 * @return
	 */
	private String[] getCommonUserAppPermissions4Menu(Trace t, Long userId, Long appId) {
		List<R> roles = getRoleDao().find4User(t, userId);
		if (roles == null || roles.isEmpty()) {
			return new String[0];
		}

		Set<String> result = new HashSet<String>();
		for (R role : roles) {
			String[] rolePerms = getRoleDao().findPermedMenuResCodes(t, role.getId(), appId);
			if (rolePerms != null && rolePerms.length > 0) {
				for (String res : rolePerms) {
					result.add(res);
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

	private String[] menu2RescodeArray(List<Menu> menus) {
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

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "修改密码([(${userId})]), TID:[(${tid})]")
	public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd)
			throws BaseRuntimeException {
		T user = this.fetchByIdUuid(t, userId, userUuid);
		Account account = accountDao.fecthByAccount(t, user.getAccount());
		if (account != null) {
			if (Securities.validePassword4Md5Src(oldPwd, account.getSalt(), account.getPassword())) {
				account.setSalt(account.generateUuid());
				account.setPassword(Securities.password4Md5Src(newPwd, account.getSalt()));
				int rows = accountDao.update(t, account);
				return retBool(rows);
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	@ServLog(db = true, desc = "验证登录账号([(${accountOrMobile})]), TID:[(${tid})]")
	public T loginValidate(Trace t, String accountOrMobile, String password) throws BaseRuntimeException {
		if (Strings.isBlank(accountOrMobile)) {
			throw new BaseRuntimeException("10002000", "登录名或者密码错误，请重新输入！");
		}
		if (Strings.isBlank(password)) {
			throw new BaseRuntimeException("10002001", "登录名或者密码错误，请重新输入！");
		}

		T dbUser = dao().fecthByAccount(t, accountOrMobile);
		if (dbUser == null) {
			if (accountOrMobile.length() == 11 && StringUtils.isNumeric(accountOrMobile)) {
				List<T> phoneUsers = dao().findByMobile(t, accountOrMobile);
				if (phoneUsers == null || phoneUsers.size() > 1) {
					throw new BaseRuntimeException("10002000", "登录名或者密码错误，请重新输入！");
				}
				dbUser = phoneUsers.get(0);
			} else {
				throw new BaseRuntimeException("10002000", "登录名或者密码错误，请重新输入！");
			}
		}

		// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());

		if (Securities.validePassword4Md5Src(password, dbUser.getSalt(), dbUser.getPassword())) {
			dao().updateLastLoginTime(t, dbUser.getId());
			return dbUser;
		}
		throw new BaseRuntimeException("10002000", "登录名或者密码错误，请重新输入！");
	}

	@Override
	protected void treeBefore(Trace t, boolean isNew, T entity) throws BaseRuntimeException {
		if (exist(t, isNew, entity, "account", entity.getAccount())) {
			throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "已经存在相同账号的用户！");
		}

		if (!Strings.isBlank(entity.getMobile())) {
			if (exist(t, isNew, entity, "mobile", entity.getMobile())) {
				throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "已经存在相同手机号码的用户！");
			}
		}

		if (isNew) {// 新增
			entity.setSalt(entity.generateUuid());
			if (Strings.isBlank(entity.getPassword())) {
				throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "密码不能为空！");
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
	protected void treeAfter(Trace t, boolean isNew, T entity) throws BaseRuntimeException {
		if (isNew) {// 新增
			parseUserCode(t, entity);
			updateCode(t, entity.getId(), entity.getCode());

			// 自动创建用户登录账号
			autoCreateAccount(t, entity);
		} else {
			if (!Strings.isBlank(entity.getPassword())) {
				Account account = accountDao.fecthByAccount(t, entity.getAccount());
				if (account != null) {
					account.setPassword(entity.getPassword());
					account.setSalt(entity.getSalt());
					accountDao.update(t, account);
				}
			}
		}

		// 自动创建用户相关其它账户，比如积分账户.若修改的时候发现没有创建，也会进行创建。
		autoCreateOtherAccount4User(t, entity);
	}

	protected void autoCreateOtherAccount4User(Trace t, T entity) {

	}

	private void parseUserCode(Trace t, T entity) {
		if (entity.getParentId() == null || Strings.isBlank(entity.getParentClass())) {
			throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "parentId或者parentClass参数错误。");
		}

		if (entity.getParentClass().endsWith("Company")) {
			C company = getCompanyDao().fetchById(t, entity.getCompanyId());
			if (company == null) {
				throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "companyId参数错误。");
			}
			entity.setCode(Domains.genDomainCode(company, entity));
		} else {
			D department = getDepartmentDao().fetchById(t, entity.getParentId());
			if (department != null) {
				entity.setCode(Domains.genDomainCode(department, entity));
			} else {
				throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
			}
		}
	}

	private void autoCreateAccount(Trace t, T entity) {
		Account account = new Account(entity.getClass().getSimpleName(), entity.getName(), entity.getMobile(),
				entity.getAccount(), entity.getPassword(), entity.getSalt());
		accountDao.insert(t, account);
	}

	@Override
	public T fecthByAZwuid(Trace t, String zwuid) {
		return dao().fecthByAZwuid(t, zwuid);
	}

	@Override
	public T fecthByDduid(Trace t, String dduid) {
		return dao().fecthByDduid(t, dduid);
	}

	@Transactional(readOnly = false)
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

	@Transactional(readOnly = false)
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

	@Transactional(readOnly = false)
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

	@Transactional(readOnly = false)
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

}
