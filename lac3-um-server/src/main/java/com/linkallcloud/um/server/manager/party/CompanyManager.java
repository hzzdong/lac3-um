package com.linkallcloud.um.server.manager.party;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.um.service.party.ICompanyService;
import com.linkallcloud.um.service.party.IDepartmentService;
import com.linkallcloud.um.service.party.IRoleService;
import com.linkallcloud.um.service.party.IUserService;
import com.linkallcloud.um.service.sys.IAreaService;
import com.linkallcloud.um.service.sys.IMenuService;

public abstract class CompanyManager<T extends Company, S extends ICompanyService<T>, D extends Department, DS extends IDepartmentService<D>, U extends User, US extends IUserService<U>, R extends Role, RS extends IRoleService<R, U>>
		extends OrgManager<T, S, U, US> implements ICompanyManager<T, U> {

	@Autowired
	protected IAreaService areaService;

	@Autowired
	protected IMenuService menuService;

	protected abstract US userService();

	protected abstract RS roleService();

	protected abstract DS departmentService();

	@Override
	public List<T> findSubCompanies(Trace t, Long companyId) {
		return service().findSubCompanies(t, companyId);
	}

	@Override
	public Long[] getCompanyAppAreaRootIds(Trace t, Long companyId, Long appId) {
		return service().getCompanyAppAreaRootIds(t, companyId, appId);
	}

	@Override
	public List<T> findDirectCompaniesByParentId(Trace t, Long parentCompanyId) {
		return service().findDirectCompaniesByParentId(t, parentCompanyId);
	}

	protected void checkMenus(Trace t, List<Tree> items, CopyOnWriteArrayList<Long> permedMenuIds) {
		if (items == null || items.isEmpty() || permedMenuIds == null || permedMenuIds.isEmpty()) {
			return;
		}
		for (Tree item : items) {
			for (Long pid : permedMenuIds) {
				if (pid.toString().equals(item.getId())) {
					item.setChecked(true);
					permedMenuIds.remove(pid);
					break;
				}
			}
		}
	}

	@Override
	public List<Tree> findPermedCompanyAppMenus(Trace t, Long myCompanyId, Long forCompanyId, Long appId) {
		List<Tree> items = service().findCompanyValidMenus(t, myCompanyId, appId);
		Long[] permedMenuIds = service().findPermedCompanyAppMenus(t, forCompanyId, appId);
		if (items != null && permedMenuIds != null && permedMenuIds.length > 0) {
			CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedMenuIds);
			checkMenus(t, items, pmids);
		}
		return items;
	}

	@Override
	public Tree findPermedAppMenusTree(Trace t, Sid myCompanyId, Sid forCompanyId, Sid appId) {
		return service().findPermedAppMenusTree(t, myCompanyId, forCompanyId, appId);
	}

	@Override
	public Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds) {
		return service().saveAppMenuPerm(t, companyId, appId, menuUuidIds);
	}

	@Override
	public Boolean saveCompanyAppMenuPerm(Trace t, Long id, String uuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds) {
		return service().saveCompanyAppMenuPerm(t, id, uuid, appId, appUuid, menuUuidIds);
	}

	@Override
	public Boolean saveCompanyAppAreaPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> uuidIds) {
		return service().saveCompanyAppAreaPerm(t, companyId, companyUuid, appId, appUuid, uuidIds);
	}

	@Override
	public boolean updateStatus(Trace t, int status, Long id, String uuid) throws BaseRuntimeException {
		if (status != 0) {
			userService().updateStatusByCompany(t, status, id);
		}
		return super.updateStatus(t, status, id, uuid);
	}

	@Override
	public Long[] getConfigCompanyAreaRootIds(Trace t, Sid companyId) {
		return service().getConfigCompanyAreaRootIds(t, companyId);
	}

	@Override
	public List<NameValue> getConfigCompanyAreaRoots(Trace t, Sid companyId) {
		return service().getConfigCompanyAreaRoots(t, companyId);
	}

	@Override
	public Long[] getCompanyAreaRootIds(Trace t, Sid companyId) {
		return service().getCompanyAreaRootIds(t, companyId);
	}

	@Override
	public Tree loadCompanyAreaFullTree(Trace t, Sid companyId) {
		return service().loadCompanyAreaFullTree(t, companyId);
	}

	@Override
	public Tree loadCompanyAreaTree(Trace t, Sid companyId) {
		return service().loadCompanyAreaTree(t, companyId);
	}

	@Override
	public Tree getPermedCompanyTree(Trace t, Long appId, Long userId) {
		return service().getPermedCompanyTree(t, appId, userId);
	}

	@Override
	public Tree getCompanyTree(Trace t, String treeType, Sid companyId) {
		return service().getCompanyTree(t, treeType, companyId);
	}

	@Override
	public Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
		return service().addApps(t, id, uuid, appUuidIds);
	}

	@Override
	public Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
		return service().removeApps(t, id, uuid, appUuidIds);
	}

}
