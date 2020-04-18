package com.linkallcloud.um.server.manager.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.party.IKhDepartmentService;
import com.linkallcloud.um.service.party.IKhRoleService;
import com.linkallcloud.um.service.party.IKhUserService;
import com.linkallcloud.um.service.party.IYwCompanyService;

@Service(interfaceClass = IKhCompanyManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "客户单位")
public class KhCompanyManager extends
		CompanyManager<KhCompany, IKhCompanyService, KhDepartment, IKhDepartmentService, KhUser, IKhUserService, KhRole, IKhRoleService>
		implements IKhCompanyManager {
	// private static Log logs = Logs.get();

	@Autowired
	private IKhCompanyService khCompanyService;

	@Autowired
	private IKhUserService khUserService;

	@Autowired
	private IYwCompanyService ywCompanyService;
	@Autowired
	private IKhDepartmentService khDepartmentService;
	@Autowired
	private IKhRoleService khRoleService;

	@Override
	protected IKhCompanyService service() {
		return khCompanyService;
	}

	@Override
	protected IKhUserService userService() {
		return khUserService;
	}

	@Override
	protected IKhRoleService roleService() {
		return khRoleService;
	}

	@Override
	protected IKhDepartmentService departmentService() {
		return khDepartmentService;
	}

	public List<KhCompany> countByArea4id(Trace t, KhCompany entity) {
		return khCompanyService.countByArea4id(t, entity);
	}

	@Override
	public Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
		return service().addApps(t, id, uuid, appUuidIds);
	}

	@Override
	public Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
		return service().removeApps(t, id, uuid, appUuidIds);
	}

	@Override
	public List<Tree> findPermedKhCompanyAppMenus(Trace t, Long ywCompanyId, Long khCompanyId, Long appId) {
		YwCompany ywCompany = ywCompanyService.fetchById(t, ywCompanyId);
		List<Tree> items = null;
		if (ywCompany.isTopParent()) {
			items = menuService.getValidMenusWithButton(t, appId);
		} else {
			items = service().findCompanyValidMenus(t, ywCompanyId, appId);
		}

		Long[] permedMenuIds = service().findPermedCompanyAppMenus(t, khCompanyId, appId);
		if (items != null && permedMenuIds != null && permedMenuIds.length > 0) {
			CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedMenuIds);
			checkMenus(t, items, pmids);
		}
		return items;
	}

	@Override
	public List<Tree> getDefinedCompanyAreas(Trace t, Long companyId, Long appId) {
		Long areaRootId = 0L;
		KhCompany company = service().fetchById(t, companyId);
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

}
