package com.linkallcloud.um.server.service.party;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.ServLog;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.activity.party.IDepartmentActivity;
import com.linkallcloud.um.activity.party.IRoleActivity;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.activity.sys.IAccountActivity;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.activity.sys.IMenuActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.service.party.IUserService;

@Module(name = "用户")
@Transactional(readOnly = true)
public abstract class UserService<T extends User, UA extends IUserActivity<T>, D extends Department, DA extends IDepartmentActivity<D>, C extends Company, CA extends ICompanyActivity<C>, R extends Role, RA extends IRoleActivity<R, T>>
		extends PartyService<T, UA> implements IUserService<T> {

	@Autowired
	protected IAccountActivity accountActivity;

	@Autowired
	protected IApplicationActivity applicationActivity;

	@Autowired
	protected IMenuActivity menuActivity;

	protected abstract DA getDepartmentActivity();

	protected abstract CA getCompanyActivity();

	protected abstract RA getRoleActivity();

	public UserService() {
		super();
	}

	@Override
	public List<T> find4Company(Trace t, Long companyId) {
		return activity().find4Company(t, companyId);
	}

	@Override
	public List<T> find4Department(Trace t, Long departmentId) {
		return activity().find4Department(t, departmentId);
	}

	@Transactional(readOnly = false)
	@ServLog(db = true)
	public Long insert(Trace t, T entity) throws BaseRuntimeException {
		return activity().insert(t, entity);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean update(Trace t, T entity) throws BaseRuntimeException {
		return activity().update(t, entity);
	}

	@Override
	public Page<T> findPermedUserPage4Select(Trace t, Page<T> page) {
		return activity().findPermedUserPage4Select(t, page);
	}

	@Override
	public List<T> findByMobile(Trace t, String mobile) {
		return activity().findByMobile(t, mobile);
	}

	@Override
	public Page<T> leaderPage(Trace t, Page<T> page) {
		return activity().leaderPage(t, page);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为用户([(${userId})]) 添加关联角色([(${roleUuidIds})]), TID:[(${tid})]")
	public boolean addUserRoles(Trace t, Long userId, String userUuid, Map<String, Long> roleUuidIds) {
		return activity().addUserRoles(t, userId, userUuid, roleUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为用户([(${userId})]) 移除关联角色([(${roleUuidIds})]), TID:[(${tid})]")
	public boolean removeUserRoles(Trace t, Long userId, String userUuid, Map<String, Long> roleUuidIds) {
		return activity().removeUserRoles(t, userId, userUuid, roleUuidIds);
	}

	@Override
	public Page<T> findPage4Role(Trace t, Page<T> page) {
		return activity().findPage4Role(t, page);
	}

	@Override
	public List<T> find4Role(Trace t, Long roleId) {
		return activity().find4Role(t, roleId);
	}

	@Override
	public List<T> find4Role(Trace t, Long[] roleIds) {
		return activity().find4Role(t, roleIds);
	}

	@Override
	public List<T> find4Role(Trace t, String roleGovCode) {
		return activity().find4Role(t, roleGovCode);
	}

	@Override
	public List<T> find4Role(Trace t, String[] roleGovCodes) {
		return activity().find4Role(t, roleGovCodes);
	}

	@Override
	public List<T> findByRoleCompany(Trace t, Long companyId, Long roleId) {
		return activity().findByRoleCompany(t, companyId, roleId);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long roleId) {
		return activity().findDepartmentUser4Role(t, departmentId, roleId);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long[] roleIds) {
		return activity().findDepartmentUser4Role(t, departmentId, roleIds);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String roleGovCode) {
		return activity().findDepartmentUser4Role(t, departmentId, roleGovCode);
	}

	@Override
	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String[] roleGovCodes) {
		return activity().findDepartmentUser4Role(t, departmentId, roleGovCodes);
	}

	@Override
	public List<Long> findUserAppPermedOrgs(Trace t, Long userId, Long appId) {
		return activity().findUserAppPermedOrgs(t, userId, appId);
	}

	// @Cacheable(value = "Permissions4AppMenu", key = "#userId + \"-\" + #appId")
	@Override
	public String getUserAppPermissions4MenuJsonString(Trace t, Long userId, Long appId) {
		return activity().getUserAppPermissions4MenuJsonString(t, userId, appId);
	}

	@Override
	public String[] getUserAppPermissions4Menu(Trace t, Long userId, Long appId) {
		return activity().getUserAppPermissions4Menu(t, userId, appId);
	}

	@Override
	public T fecthByAccount(Trace t, String account) {
		return activity().fecthByAccount(t, account);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "修改密码([(${userId})]), TID:[(${tid})]")
	public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd) {
		return activity().updatePassword(t, userId, userUuid, oldPwd, newPwd);
	}

	@Override
	@Transactional(readOnly = false)
	@ServLog(db = true, desc = "验证登录账号([(${accountOrMobile})]), TID:[(${tid})]")
	public T loginValidate(Trace t, String accountOrMobile, String password) {
		return activity().loginValidate(t, accountOrMobile, password);
	}

	@Override
	public T fecthByAZwuid(Trace t, String zwuid) {
		return activity().fecthByAZwuid(t, zwuid);
	}

	@Override
	public T fecthByDduid(Trace t, String dduid) {
		return activity().fecthByDduid(t, dduid);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updateZwuid(Trace t, Long umUserId, String zwuid) {
		return activity().updateZwuid(t, umUserId, zwuid);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updateDduid(Trace t, Long umUserId, String dduid, int ddStatus) {
		return activity().updateDduid(t, umUserId, dduid, ddStatus);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updateStatusByCompany(Trace t, int status, Long companyId) {
		return activity().updateStatusByCompany(t, status, companyId);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updateStatusByDepartment(Trace t, int status, Long departmentId) {
		return activity().updateStatusByDepartment(t, status, departmentId);
	}

	@Override
	public T fetchCompanyAdmin(Trace t, Sid companyId) {
		return activity().fetchCompanyAdmin(t, companyId);
	}

}
