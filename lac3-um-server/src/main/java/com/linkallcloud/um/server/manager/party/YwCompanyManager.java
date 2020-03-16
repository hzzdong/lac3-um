package com.linkallcloud.um.server.manager.party;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwRoleService;
import com.linkallcloud.um.service.party.IYwUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.Service;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;

@Service(interfaceClass = IYwCompanyManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "运维单位")
public class YwCompanyManager extends
		CompanyManager<YwCompany, IYwCompanyService, YwDepartment, IYwDepartmentService, YwUser, IYwUserService, YwRole, IYwRoleService>
		implements IYwCompanyManager {

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Autowired
	private IYwUserService ywUserService;

	@Autowired
	private IYwRoleService ywRoleService;

	@Autowired
	private IYwDepartmentService ywDepartmentService;

	@Override
	protected IYwCompanyService service() {
		return ywCompanyService;
	}

	@Override
	protected IYwUserService userService() {
		return ywUserService;
	}

	@Override
	protected IYwRoleService roleService() {
		return ywRoleService;
	}

	@Override
	protected IYwDepartmentService departmentService() {
		return ywDepartmentService;
	}

	@Override
	public List<Tree> getDefinedCompanyAreas(Trace t, Long companyId, Long appId) {
		Long areaRootId = 0L;
		YwCompany company = service().fetchById(t, companyId);
		if (company.isTopParent()) {
			areaRootId = service().getCompanyAreaRootIdBySystemConfig(t, companyId);
			if (areaRootId != null && areaRootId.longValue() > 0) {
				return areaService.findChildrenTreeNodes(t, areaRootId, new Equal("status", 0));
			} else {
				return areaService.getTreeNodes(t, true);
			}
		} else {
			List<Area> permedAreas = areaService.findPermedYwCompanyAppAreas(t, companyId, appId);
			List<Tree> result = new ArrayList<>();
			if (permedAreas != null && !permedAreas.isEmpty()) {
//				Area rootArea = areaService.fetchById(t, permedAreas.get(0).getParentId());
//
//				Tree root = rootArea.toTreeNode();
//				root.setpId(null);
//				root.setOpen(true);

				for (Area pa : permedAreas) {
					List<Area> areas = areaService.findChildren(t, pa.getCode(), new Equal("status", 0));
					for (Area area : areas) {
						Tree node = area.toTreeNode();
						result.add(node);
					}
				}
				//result.add(root);
			}
			return result;
		}
	}

}
