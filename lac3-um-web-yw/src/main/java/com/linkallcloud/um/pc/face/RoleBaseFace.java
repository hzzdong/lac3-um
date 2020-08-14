package com.linkallcloud.um.pc.face;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.face.message.request.ParentIdFaceRequest;
import com.linkallcloud.core.face.message.request.RelFaceRequest;
import com.linkallcloud.core.face.message.request.RelParentIdFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.um.iapi.party.IDepartmentManager;
import com.linkallcloud.um.iapi.party.IRoleManager;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

public abstract class RoleBaseFace<R extends Role, U extends User, S extends IRoleManager<R, U>, US extends IUserManager<U>, C extends Company, CS extends ICompanyManager<C, U>, D extends Department, DS extends IDepartmentManager<D, U>>
		extends BaseFace<R, S> {

	public RoleBaseFace() {
		super();
	}

	protected abstract CS companyManager();

	protected abstract DS departmentManager();

	protected abstract US userManager();

	protected abstract int getRoleType();

	@Face(simple = true)
	@RequestMapping(value = "/findCompanyAllRolePage", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyAllRolePage(PageFaceRequest fr, Trace t, SessionUser suser) {
		Page<R> page = new Page<>(fr);
		return manager().findCompanyAllRolePage(t, suser.companyId(), page);
	}

	@Face(simple = true)
	@RequestMapping(value = "/findCompanyRolePage", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyRolePage(PageFaceRequest fr, Trace t, SessionUser suser) {
		int type = getRoleType();
		Page<R> page = new Page<>(fr);
		return manager().findCompanyRolePage(t, suser.companyId(), type, page);
	}

	@Face(simple = true)
	@RequestMapping(value = "/findCompanyRoles", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyRoles(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return findCompanyRoles4Me(t, null, fr.getParentId(), fr.getParentClass(), fr.getId(), fr.getUuid());
	}

	@Face(simple = true)
	@RequestMapping(value = "/findJzCompanyRoles", method = RequestMethod.POST)
	public @ResponseBody Object findJzCompanyRoles(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return findCompanyRoles4Me(t, suser.companyId(), fr.getParentId(), fr.getParentClass(), fr.getId(),
				fr.getUuid());
	}

	@Face(simple = true)
	@RequestMapping(value = "/findUserRoleIds", method = RequestMethod.POST)
	public @ResponseBody Object findUserRoleIds(IdFaceRequest fr, Trace t, SessionUser suser) {
		List<R> roles = findUserRoles(t, fr.getId(), null);
		List<Long> ids = Domains.getIds(roles);
		return ids;
	}

	@Face(simple = true)
	@RequestMapping(value = "/findJzUserRoleIds", method = RequestMethod.POST)
	public @ResponseBody Object findJzUserRoleIds(IdFaceRequest fr, Trace t, SessionUser suser) {
		List<R> roles = findUserRoles(t, fr.getId(), suser.companyId());
		List<Long> ids = Domains.getIds(roles);
		return ids;
	}

	@Face(simple = true)
	@RequestMapping(value = "/findUserRoles", method = RequestMethod.POST)
	public @ResponseBody Object findUserRoles(IdFaceRequest fr, Trace t, SessionUser suser) {
		List<R> roles = findUserRoles(t, fr.getId(), null);
		return roles;
	}

	@Face(simple = true)
	@RequestMapping(value = "/findJzUserRoles", method = RequestMethod.POST)
	public @ResponseBody Object findJzUserRoles(IdFaceRequest fr, Trace t, SessionUser suser) {
		List<R> roles = findUserRoles(t, fr.getId(), suser.companyId());
		return roles;
	}

	@LacLog(desc = "用户([(${su.sid.name})])把角色 [(${fr.id})]分配给了新用户, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/addUsers", method = RequestMethod.POST)
	public @ResponseBody Object addUsers(RelFaceRequest fr, Trace t, SessionUser suser) {
		return doAddRoleUsers(t, fr, suser.companyId());
	}

	protected Boolean doAddRoleUsers(Trace t, RelFaceRequest fr, Long companyId) {
		return manager().addRoleUsers(t, fr.getId(), fr.getUuid(), fr.getUuidIds(), companyId);
	}

	@LacLog(desc = "用户([(${su.sid.name})])取消了部分用户的角色 [(${fr.id})], TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	public @ResponseBody Object removeUser(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return doRemoveRoleUsers(t, fr, suser.companyId());
	}

	protected Boolean doRemoveRoleUsers(Trace t, ParentIdFaceRequest fr, Long companyId) {
		Map<String, Long> userUuidIds = new HashMap<String, Long>();
		userUuidIds.put(fr.getUuid(), fr.getId());
		return manager().removeRoleUsers(t, fr.getParentId(), fr.getParentUuid(), userUuidIds, companyId);
	}

	@LacLog(desc = "用户([(${su.sid.name})])给角色 [(${fr.id})]许可了新应用, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/addApps", method = RequestMethod.POST)
	public @ResponseBody Object addApps(RelFaceRequest fr, Trace t, SessionUser suser) {
		return manager().addRoleApps(t, fr.getId(), fr.getUuid(), fr.getUuidIds());
	}

	@LacLog(desc = "用户([(${su.sid.name})])取消了角色 [(${fr.id})]部分应用许可, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/removeApp", method = RequestMethod.POST)
	public @ResponseBody Object removeApp(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Map<String, Long> appUuidIds = new HashMap<String, Long>();
		appUuidIds.put(fr.getUuid(), fr.getId());
		return manager().removeRoleApps(t, fr.getParentId(), fr.getParentUuid(), appUuidIds);
	}

	@Face(simple = true)
	@RequestMapping(value = "/getPermedMenuTree", method = RequestMethod.POST)
	public @ResponseBody Object getPermedMenuTree(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Tree tree = manager().findPermedMenuTree(t, suser.companyId(), fr.getParentId(), fr.getId());
		return tree.getChildren();
	}

	@LacLog(desc = "用户([(${su.sid.name})])修改了角色([(${fr.parentId})]) 应用([(${fr.id})]) 的菜单权限, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/saveRoleAppMenuPerm", method = RequestMethod.POST)
	public @ResponseBody Object saveRoleAppMenuPerm(RelParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return manager().saveRoleAppMenuPerm(t, fr.getParentId(), fr.getParentUuid(), fr.getId(), fr.getUuid(),
				fr.getUuidIds());
	}

	@Face(simple = true)
	@RequestMapping(value = "/getPermedOrgTree", method = RequestMethod.POST)
	public @ResponseBody Object getPermedOrgTree(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Tree tree = manager().findPermedOrgTree(t, suser.companyId(), fr.getParentId(), fr.getId());
		// return tree.getChildren();
		return Arrays.asList(tree);
	}

	@LacLog(desc = "用户([(${su.sid.name})])修改了角色([(${fr.parentId})]) 应用([(${fr.id})]) 的机构权限, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/saveRoleAppOrgPerm", method = RequestMethod.POST)
	public @ResponseBody Object saveRoleAppOrgPerm(RelParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return manager().saveRoleAppOrgPerm(t, fr.getParentId(), fr.getParentUuid(), fr.getId(), fr.getUuid(),
				fr.getUuidIds());
	}

	@Face(simple = true)
	@RequestMapping(value = "/getPermedAreaTree", method = RequestMethod.POST)
	public @ResponseBody Object getPermedAreaTree(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Tree tree = manager().findPermedAreaTree(t, suser.getCompany(), new Sid(fr.getParentId(), fr.getParentUuid()),
				new Sid(fr.getId(), fr.getUuid()));
		return tree.getChildren();
	}

	@LacLog(desc = "用户([(${su.sid.name})])修改了角色([(${fr.parentId})]) 应用([(${fr.id})]) 的区域权限, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/saveRoleAppAreaPerm", method = RequestMethod.POST)
	public @ResponseBody Object saveRoleAppAreaPerm(RelParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return manager().saveRoleAppAreaPerm(t, fr.getParentId(), fr.getParentUuid(), fr.getId(), fr.getUuid(),
				fr.getUuidIds());
	}

	protected List<R> findCompanyRoles(Trace t, Long companyId, Integer roleLevel) {
		return manager().findCompanyRolesByLevel(t, companyId, roleLevel);
	}

	protected List<R> findCompanyRolesByDepartmentId(Trace t, Long departmentId) {
		Department depart = departmentManager().fetchById(t, departmentId);
		if (depart != null) {
			return manager().findCompanyRolesByLevel(t, depart.getCompanyId(), 1 == depart.getMdep() ? 9 : null);
		}
		return null;
	}

	protected List<R> findCompanyRoles4Me(Trace t, Long operatorCompanyId, Long parentId, String parentClass,
			Long userId, String userUuid) {
		List<R> roles = null;
		if (parentId != null && !Strings.isBlank(parentClass)) {
			if (parentClass.endsWith("Company")) {
				roles = findCompanyRoles(t, parentId, 9);
			} else if (parentClass.endsWith("Department")) {
				roles = findCompanyRolesByDepartmentId(t, parentId);
			}
		} else {
			if (userId != null) {
				boolean isAdminDepartment = false;
				U user = userManager().fetchById(t, userId);
				if (user != null) {
					if (user.getParentClass().endsWith("Company")) {
						isAdminDepartment = true;
					} else {
						Department depart = departmentManager().fetchById(t, user.getParentId());
						if (depart != null) {
							isAdminDepartment = 1 == depart.getMdep();
						}
					}
					if (operatorCompanyId == null) {
						operatorCompanyId = user.getCompanyId();
					}
					roles = findCompanyRoles(t, operatorCompanyId, isAdminDepartment == true ? 9 : null);
				}
			} else {
				roles = findCompanyRoles(t, operatorCompanyId, 9);
			}
		}

		if (roles != null && !roles.isEmpty() && userId != null) {
			List<R> userRoles = findUserRoles(t, userId, operatorCompanyId);
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

	protected List<R> findUserRoles(Trace t, Long userId, Long operatorCompanyId) {
		return manager().find4User(t, userId, operatorCompanyId);
	}

	@Override
	protected void doSave(Trace t, R entity, SessionUser suser) {
		entity.setCompanyId(suser.companyId());

		entity.setType(getRoleType());
		if (entity.getType() == Domains.ROLE_NORMAL) {
			entity.setParentId(suser.companyId());
		} else {
			entity.setParentId(0L);
		}
		super.doSave(t, entity, suser);
	}

}
