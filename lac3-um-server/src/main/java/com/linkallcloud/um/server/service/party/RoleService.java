package com.linkallcloud.um.server.service.party;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.ServLog;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.activity.party.IRoleActivity;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.service.party.IRoleService;

@Module(name = "角色")
@Transactional(readOnly = true)
public abstract class RoleService<R extends Role, U extends User, C extends Company, RA extends IRoleActivity<R, U>, UA extends IUserActivity<U>, CA extends ICompanyActivity<C>>
		extends PartyService<R, RA> implements IRoleService<R, U> {

	protected Mirror<U> userMirror;
	protected Mirror<C> companyMirror;

	@SuppressWarnings("unchecked")
	public RoleService() {
		super();
		try {
			userMirror = Mirror.me((Class<U>) Mirror.getTypeParams(getClass())[1]);
			companyMirror = Mirror.me((Class<C>) Mirror.getTypeParams(getClass())[2]);
		} catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("!!!Fail to get TypeParams for self!", e);
			}
		}
	}

	public Class<U> getUserClass() {
		return (null == userMirror) ? null : userMirror.getType();
	}

	public Class<C> getCompanyClass() {
		return (null == companyMirror) ? null : companyMirror.getType();
	}

	public abstract UA userActivity();

	public abstract CA comapnyActivity();

	public abstract IApplicationActivity applicationActivity();

	@Override
	public List<R> find4User(Trace t, Long userId, Long companyId) {
		return activity().find4User(t, userId, companyId);
	}

	@Override
	public Page<R> findCompanyRolePage(Trace t, Long companyId, int type, Page<R> page) {
		return activity().findCompanyRolePage(t, companyId, type, page);
	}

	@Override
	public List<R> findCompanyRoles(Trace t, Long companyId, int type) {
		return activity().findCompanyRoles(t, companyId, type);
	}

	@Override
	public List<R> findCompanyAllRoles(Trace t, Long companyId) {
		return activity().findCompanyAllRoles(t, companyId);
	}

	@Override
	public List<R> findCompanyRolesByLevel(Trace t, Long companyId, Integer roleLevel) {
		return activity().findCompanyRolesByLevel(t, companyId, roleLevel);
	}

	@Override
	public Page<R> findCompanyAllRolePage(Trace t, Long companyId, Page<R> page) {
		return activity().findCompanyAllRolePage(t, companyId, page);
	}

	@Override
	public Page<R> findNoRolePage4User(Trace t, Page<R> page) {
		return activity().findNoRolePage4User(t, page);
	}

	@Override
	public Page<R> findPage4User(Trace t, Page<R> page) {
		return activity().findPage4User(t, page);
	}

	@Override
	public List<U> getRoleUsers(Trace t, Long roleId, String roleUuid) {
		return activity().getRoleUsers(t, roleId, roleUuid);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})]) 添加关联用户([(${userUuidIds})]), TID:[(${t.tid})]")
	public boolean addRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds, Long companyId) {
		return activity().addRoleUsers(t, roleId, roleUuid, userUuidIds, companyId);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})]) 移除关联用户([(${userUuidIds})]), TID:[(${t.tid})]")
	public boolean removeRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds, Long companyId) {
		return activity().removeRoleUsers(t, roleId, roleUuid, userUuidIds, companyId);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})]) 添加许可应用([(${appUuidIds})]), TID:[(${t.tid})]")
	public boolean addRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds) {
		return activity().addRoleApps(t, roleId, roleUuid, appUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})]) 移除许可应用([(${appUuidIds})]), TID:[(${t.tid})]")
	public boolean removeRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds) {
		return activity().removeRoleApps(t, roleId, roleUuid, appUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})])保存应用([(${appId})])的菜单权限([(${menuUuidIds})]), TID:[(${t.tid})]")
	public Boolean saveRoleAppMenuPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds) {
		return activity().saveRoleAppMenuPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})])保存应用([(${appId})])的菜单权限([(${menuUuidIds})]), TID:[(${t.tid})]")
	public Boolean saveRoleAppOrgPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
			Map<String, Long> appUuidIds) {
		return activity().saveRoleAppOrgPerm(t, roleId, roleUuid, appId, appUuid, appUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean saveRoleAppAreaPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
			Map<String, Long> areaUuidIds) {
		return activity().saveRoleAppAreaPerm(t, roleId, roleUuid, appId, appUuid, areaUuidIds);
	}

	@Override
	public Long[] findPermedAreaIds(Trace t, Long roleId, Long appId) {
		return activity().findPermedAreaIds(t, roleId, appId);
	}

	@Override
	public Long[] findPermedOrgIds(Trace t, Long roleId, Long appId) {
		return activity().findPermedOrgIds(t, roleId, appId);
	}

	@Override
	public Long[] findPermedMenuIds(Trace t, Long roleId, Long appId) {
		return activity().findPermedMenuIds(t, roleId, appId);
	}

	@Override
	public String[] findPermedMenuResCodes(Trace t, Long roleId, Long appId) {
		return activity().findPermedMenuResCodes(t, roleId, appId);
	}

	@Transactional(readOnly = false)
	@Override
	@ServLog(db = true, desc = "为角色([(${roleId})]) 清空关联用户, TID:[(${tid})]")
	public boolean clearRoleUsers(Trace t, Long roleId, String roleUuid) {
		return activity().clearRoleUsers(t, roleId, roleUuid);
	}

//	@Override
//	public List<R> findCompanyRoles4Me(Trace t, long userId) {
//		U user = userActivity().fetchById(t, userId);
//		if (user != null) {
//
//		}
//		return null;
//	}

}
