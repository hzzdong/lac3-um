package com.linkallcloud.um.server.manager.party;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.IRoleManager;
import com.linkallcloud.um.service.party.ICompanyService;
import com.linkallcloud.um.service.party.IRoleService;
import com.linkallcloud.um.service.sys.IAreaService;

public abstract class RoleManager<T extends Role, U extends User, S extends IRoleService<T, U>, C extends Company, CS extends ICompanyService<C>>
		extends PartyManager<T, S> implements IRoleManager<T, U> {

	protected static Log logs = Logs.get();

	@Autowired
	protected IAreaService areaService;

	protected abstract CS companyService();

	@Override
	public Page<T> findCompanyRolePage(Trace t, Long companyId, int type, Page<T> page) throws BaseRuntimeException {
		return service().findCompanyRolePage(t, companyId, type, page);
	}

	@Override
	public List<T> findCompanyRoles(Trace t, Long companyId, int type) throws BaseRuntimeException {
		return service().findCompanyRoles(t, companyId, type);
	}

	@Override
	public List<T> findCompanyAllRoles(Trace t, Long companyId) throws BaseRuntimeException {
		return service().findCompanyAllRoles(t, companyId);
	}

	@Override
	public List<T> findCompanyRolesByLevel(Trace t, Long companyId, Integer roleLevel) throws BaseRuntimeException {
		return service().findCompanyRolesByLevel(t, companyId, roleLevel);
	}

	@Override
	public Page<T> findCompanyAllRolePage(Trace t, Long companyId, Page<T> page) throws BaseRuntimeException {
		return service().findCompanyAllRolePage(t, companyId, page);
	}

	@Override
	public Page<T> findPage4User(Trace t, Page<T> page) {
		return service().findPage4User(t, page);
	}

	@Override
	public Page<T> findNoRolePage4User(Trace t, Page<T> page) {
		return service().findNoRolePage4User(t, page);
	}

	@Override
	public List<T> find4User(Trace t, Long userId) {
		return service().find4User(t, userId);
	}

	@Override
	public boolean addRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds) {
		return service().addRoleUsers(t, roleId, roleUuid, userUuidIds);
	}

	@Override
	public boolean removeRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds) {
		return service().removeRoleUsers(t, roleId, roleUuid, userUuidIds);
	}

	@Override
	public boolean clearRoleUsers(Trace t, Long roleId, String roleUuid) {
		return service().clearRoleUsers(t, roleId, roleUuid);
	}

	@Override
	public List<U> getRoleUsers(Trace t, Long roleId, String roleUuid) {
		return service().getRoleUsers(t, roleId, roleUuid);
	}

	@Override
	public boolean addRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds) {
		return service().addRoleApps(t, roleId, roleUuid, appUuidIds);
	}

	@Override
	public boolean removeRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds) {
		return service().removeRoleApps(t, roleId, roleUuid, appUuidIds);
	}

	@Override
	public Boolean saveRoleAppMenuPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds) {
		return service().saveRoleAppMenuPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
	}

	@Override
	public Boolean saveRoleAppOrgPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
			Map<String, Long> orgUuidIds) {
		return service().saveRoleAppOrgPerm(t, roleId, roleUuid, appId, appUuid, orgUuidIds);
	}

	@Override
	public Boolean saveRoleAppAreaPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
			Map<String, Long> areaUuidIds) {
		return service().saveRoleAppAreaPerm(t, roleId, roleUuid, appId, appUuid, areaUuidIds);
	}

	@Override
	public Tree findPermedMenuTree(Trace t, Long companyId, Long roleId, Long appId) {
		Tree tree = companyService().loadCompanyMenuTree(t, companyId, appId);
		Long[] permedItemIds = service().findPermedMenuIds(t, roleId, appId);
		Trees.checked(tree, permedItemIds);
		return tree;
	}

	@Override
	public Tree findPermedOrgTree(Trace t, Long companyId, Long roleId, Long appId) {
		Tree tree = companyService().loadCompanyOrgTree(t, companyId);
		Long[] permedItemIds = service().findPermedOrgIds(t, roleId, appId);
		Trees.checked(tree, permedItemIds);
		return tree;
	}

	@Override
	public Tree findPermedAreaTree(Trace t, Sid companyId, Sid roleId, Sid appId) {
		Tree tree = companyService().loadCompanyAreaTree(t, companyId);
		Long[] permedItemIds = service().findPermedAreaIds(t, roleId.getId(), appId.getId());
		Trees.checked(tree, permedItemIds);
		return tree;
	}

}
