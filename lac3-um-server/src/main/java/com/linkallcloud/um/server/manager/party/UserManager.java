package com.linkallcloud.um.server.manager.party;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.um.service.party.ICompanyService;
import com.linkallcloud.um.service.party.IRoleService;
import com.linkallcloud.um.service.party.IUserService;
import com.linkallcloud.um.service.sys.IApplicationService;
import com.linkallcloud.um.service.sys.IMenuService;

public abstract class UserManager<T extends User, S extends IUserService<T>, R extends Role, RS extends IRoleService<R, T>, C extends Company, CS extends ICompanyService<C>>
		extends PartyManager<T, S> implements IUserManager<T> {

	@Autowired
	protected IApplicationService applicationService;

	@Autowired
	protected IMenuService menuService;

	protected abstract RS roleService();

	protected abstract CS companyService();

	@Override
	public List<T> find4Company(Trace t, Long companyId) {
		return service().find4Company(t, companyId);
	}

	@Override
	public List<T> find4Department(Trace t, Long departmentId) {
		return service().find4Department(t, departmentId);
	}

	@Override
	public List<T> findByMobile(Trace t, String mobile) {
		return service().findByMobile(t, mobile);
	}

	@Override
	public T fecthByAccount(Trace t, String account) {
		return service().fecthByAccount(t, account);
	}

	@Override
	public T loginValidate(Trace t, String accountOrMobile, String password) throws BaseRuntimeException {
		return service().loginValidate(t, accountOrMobile, password);
	}

	@Override
	public Boolean updateHeaderImage(Trace t, Sid user, String ico) {
		return service().updateHeaderImage(t, user, ico);
	}

	@Override
	public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd)
			throws BaseRuntimeException {
		return service().updatePassword(t, userId, userUuid, oldPwd, newPwd);
	}

	// @Cacheable(value = "Permissions4AppMenu", key = "#userId + \"-\" + #appId")
	@Override
	public String[] getUserAppMenus(Trace t, Long companyId, Long userId, Long appId) {
		return service().getUserAppMenus(t, companyId, userId, appId);
//        String[] result = new String[0];
//        String jsonStr = service().getUserAppPermissions4MenuJsonString(t, userId, appId);
//        if (!Strings.isBlank(jsonStr)) {
//            List<String> list = JSON.parseArray(jsonStr, String.class);
//            result = list.toArray(new String[list.size()]);
//        }
//        return result;
	}

	@Override
	public List<Long> getUserAppOrgs(Trace t, Long companyId, Long userId, Long appId) {
		return service().getUserAppOrgs(t, companyId, userId, appId);
	}

	@Override
	public List<Long> getUserAppAreas(Trace t, Long companyId, Long userId, Long appId) {
		return service().getUserAppAreas(t, companyId, userId, appId);
	}

	// @Cacheable(value = "AppMenu", key = "#userId + \"-\" + #appId")
	@Override
	public Tree getUserAppMenu(Trace t, Long companyId, Long userId, Long appId) {
		// return service().getUserAppMenu(t, userId, userUuid, appId, appUuid);

		T user = service().fetchById(t, userId);
		Application app = applicationService.fetchById(t, appId);
		if (app == null || user == null) {
			return null;
		}

		Tree root = app.toMenuRoot();
		if (user.getAccount().equals("superadmin")) {
			List<Menu> menus = menuService.findAppMenus(t, appId, true);
			if (menus != null) {
				// Trees.assembleTree(root, new CopyOnWriteArrayList<Menu>(menus));
				Trees.assembleDomain2Tree(root, menus);
			}
		} else if (user.isAdmin()) {
			root = companyService().loadCompanyMenuTree(t, user.getCompanyId(), appId);
		} else {
			dealCommonUserAppMenu(t, companyId, userId, appId, root);
		}
		root.sort();
		return root;
	}

	/**
	 * 处理普通用户的应用菜单
	 *
	 * @param t
	 * @param companyId
	 * @param userId
	 * @param appId
	 * @param root
	 */
	private void dealCommonUserAppMenu(Trace t, Long companyId, Long userId, Long appId, Tree root) {
		List<R> roles = roleService().find4User(t, userId, companyId);
		if (roles != null && !roles.isEmpty()) {
			Map<Long, Menu> menuMap = new HashMap<>();
			for (R role : roles) {
				List<Menu> rolePerms = roleService().findPermedMenus(t, role.getId(), appId);
				if (rolePerms != null && rolePerms.size() > 0) {
					for (Menu menu : rolePerms) {
						if (!menuMap.containsKey(menu.getId())) {
							menuMap.put(menu.getId(), menu);
						}
					}
				}
			}
			Collection<Menu> menus = menuMap.values();
			if (menus != null) {
				// Trees.assembleTree(root, new CopyOnWriteArrayList<Menu>(menus));
				Trees.assembleDomain2Tree(root, menus);
			}
		}
	}

	@Override
	public Page<T> findPage4Role(Trace t, Page<T> page) {
		return service().findPage4Role(t, page);
	}

	@Override
	public Page<T> findPage4UnRole(Trace t, Page<T> page) {
		return service().findPage4UnRole(t, page);
	}

//	@Override
//	public List<T> find4Role(Trace t, Long roleId) {
//		return service().find4Role(t, roleId);
//	}
//
//	@Override
//	public List<T> find4Role(Trace t, Long[] roleIds) {
//		return service().find4Role(t, roleIds);
//	}
//
//	@Override
//	public List<T> find4Role(Trace t, String roleGovCode) {
//		return service().find4Role(t, roleGovCode);
//	}
//
//	@Override
//	public List<T> find4Role(Trace t, String[] roleGovCodes) {
//		return service().find4Role(t, roleGovCodes);
//	}

//	@Override
//	public List<T> findByRoleCompany(Trace t, Long companyId, Long roleId) {
//		return service().findByRoleCompany(t, companyId, roleId);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long roleId) {
//		return service().findDepartmentUser4Role(t, departmentId, roleId);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long[] roleIds) {
//		return service().findDepartmentUser4Role(t, departmentId, roleIds);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String roleGovCode) {
//		return service().findDepartmentUser4Role(t, departmentId, roleGovCode);
//	}
//
//	@Override
//	public List<T> findDepartmentUser4Role(Trace t, Long departmentId, String[] roleGovCodes) {
//		return service().findDepartmentUser4Role(t, departmentId, roleGovCodes);
//	}

	@Override
	public boolean addUserRoles(Trace t, Long userId, Long companyId, Map<String, Long> roleUuidIds) {
		boolean result = service().addUserRoles(t, userId, companyId, roleUuidIds);
		if (result) {
			List<Application> apps = findApplicaitons4User(t, userId);
			if (apps != null && !apps.isEmpty()) {
				for (Application app : apps) {
					cleanCache4UserAppPermissions4Menu(t, companyId, userId, app.getId());
					cleanCache4UserAppMenu(t, companyId, userId, app.getId());
				}
			}
		}
		return result;
	}

	@CacheEvict(value = "Permissions4AppMenu", key = "#companyId + \"-\" + #userId + \"-\" + #appId")
	@Override
	public void cleanCache4UserAppPermissions4Menu(Trace t, Long companyId, Long userId, Long appId) {
	}

	@CacheEvict(value = "AppMenu", key = "#companyId + \"-\" + #userId + \"-\" + #appId")
	@Override
	public void cleanCache4UserAppMenu(Trace t, Long companyId, Long userId, Long appId) {
	}

	protected abstract List<Application> findApplicaitons4User(Trace t, Long userId);

	@Override
	public boolean removeUserRoles(Trace t, Long userId, Long companyId, Map<String, Long> roleUuidIds) {
		List<Application> apps = findApplicaitons4User(t, userId);
		if (apps != null && !apps.isEmpty()) {
			for (Application app : apps) {
				cleanCache4UserAppPermissions4Menu(t, companyId, userId, app.getId());
				cleanCache4UserAppMenu(t, companyId, userId, app.getId());
			}
		}

		boolean result = service().removeUserRoles(t, userId, companyId, roleUuidIds);
		return result;
	}

	@Override
	public Page<T> findPermedUserPage4Select(Trace t, Page<T> page) {
		return service().findPermedUserPage4Select(t, page);
	}

	@Override
	public T fecthByAZwuid(Trace t, String zwuid) {
		return service().fecthByAZwuid(t, zwuid);
	}

	@Override
	public T fecthByDduid(Trace t, String dduid) {
		return service().fecthByDduid(t, dduid);
	}

	@Override
	public boolean updateZwuid(Trace t, Long umUserId, String zwuid) {
		return service().updateZwuid(t, umUserId, zwuid);
	}

	@Override
	public boolean updateDduid(Trace t, Long umUserId, String dduid, int ddStatus) {
		return service().updateDduid(t, umUserId, dduid, ddStatus);
	}

	@Override
	public boolean updateStatus(Trace t, int status, Long id, String uuid) throws BaseRuntimeException {
//		String accessToken = zzdUserService.getAccessToken();
//		Long orgNumber = null;//节点编号
//		// TODO 检查浙政钉是否已经开户，
//		String mobile = "";
//		Long userId = zzdUserService.getSingleUserIdByMobile(accessToken, mobile);
//		if(userId == null) {
//			throw new BaseRuntimeException("该用户在浙政钉尚未开户");
//		} 
//		if (status == 0) {// 启用
//			// TODO 兼职到虚拟目录
//			String result = zzdUserService.associate(accessToken, orgNumber, userId);
//			if(!StringUtils.isBlank(result) && "failed".equals(result)) {
//				throw new BaseRuntimeException("兼职到虚拟目录操作失败");
//			}
//		} else { // 禁用
//			// TODO 从虚拟目录离职
//			String result = zzdUserService.disassociate(accessToken, orgNumber, userId);
//			if(!StringUtils.isBlank(result) && "failed".equals(result)) {
//				throw new BaseRuntimeException("从虚拟目录离职操作失败");
//			}
//		}
		return super.updateStatus(t, status, id, uuid);
	}

	@Override
	public T fetchCompanyAdmin(Trace t, Sid companyId) {
		return service().fetchCompanyAdmin(t, companyId);
	}

	@Override
	public Page<T> findUserPage4Org(Trace t, Page<T> page) {
		return service().findUserPage4Org(t, page);
	}

	@Override
	public Page<T> findPermedUserPage(Trace t, Page<T> page) {
		return service().findPermedUserPage(t, page);
	}

}
