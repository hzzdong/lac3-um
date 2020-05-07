package com.linkallcloud.um.server.manager.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.dto.base.PermedAreaVo;
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
	public Long getCompanyAreaRootId(Trace t, Long companyId, Long appId) {
		return service().getCompanyAreaRootId(t, companyId, appId);
	}

	@Override
	public Long[] getCompanyAppAreaRootIds(Trace t, Long companyId, Long appId) {
		return service().getCompanyAppAreaRootIds(t, companyId, appId);
	}

	@Override
	public List<Tree> getDefinedCompanyAreas(Trace t, Long companyId, Long appId) {
		Long areaRootId = 0L;
		T company = service().fetchById(t, companyId);
		if (company.isTopParent()) {
			areaRootId = service().getCompanyAreaRootIdBySystemConfig(t, companyId);
			if (areaRootId != null && areaRootId.longValue() > 0) {
				return areaService.findChildrenTreeNodes(t, areaRootId, new Equal("status", 0));
			} else {
				return areaService.getTreeNodes(t, true);
			}
		} else {
			List<Area> permedAreas = areaService.findPermedKhCompanyAppAreas(t, companyId, appId);
			List<Tree> result = new ArrayList<>();
			if (permedAreas != null && !permedAreas.isEmpty()) {
				Area rootArea = areaService.fetchById(t, permedAreas.get(0).getParentId());

				Tree root = rootArea.toTreeNode();
				root.setpId(null);
				root.setOpen(true);

				for (Area pa : permedAreas) {
					List<Area> areas = areaService.findChildren(t, pa.getCode(), new Equal("status", 0));
					for (Area area : areas) {
						Tree node = area.toTreeNode();
						result.add(node);
					}
				}
				result.add(root);
			}
			return result;
		}
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
	public PermedAreaVo findPermedCompanyAppAreas(Trace t, Long myCompanyId, Long forCompanyId, Long parentAreaId,
			Long appId) {
		Long[] permedItemIds = service().findPermedCompanyAppAreas(t, forCompanyId, appId);

		PermedAreaVo result = null;
		if (parentAreaId != null && parentAreaId.longValue() > 0) {
			Long companyRootAreaId = service().getCompanyAreaRootId(t, myCompanyId, appId);
			if (parentAreaId.equals(companyRootAreaId)) {
				result = service().findCompanyValidAreaResource(t, myCompanyId, appId);
			} else {
				result = areaService.findValidAreaResourceByParent(t, parentAreaId);
			}
		} else {
			if (permedItemIds != null && permedItemIds.length > 0) {
				Area area = areaService.fetchById(t, permedItemIds[0]);
				parentAreaId = area.getParentId();
				Long companyRootAreaId = service().getCompanyAreaRootId(t, myCompanyId, appId);
				if (parentAreaId.equals(companyRootAreaId)) {
					result = service().findCompanyValidAreaResource(t, myCompanyId, appId);
				} else {
					result = areaService.findValidAreaResourceByParent(t, parentAreaId);
				}
			} else {
				result = service().findCompanyValidAreaResource(t, myCompanyId, appId);
			}
		}

		if (result.getAreaNodes() != null && result.getAreaNodes().size() > 0 && permedItemIds != null
				&& permedItemIds.length > 0) {
			CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedItemIds);
			checkMenus(t, result.getAreaNodes(), pmids);
		}
		return result;
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

}
