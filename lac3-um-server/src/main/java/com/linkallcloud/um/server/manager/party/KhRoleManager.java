package com.linkallcloud.um.server.manager.party;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.party.IKhRoleService;
import com.linkallcloud.um.service.sys.IApplicationService;
import com.linkallcloud.um.service.sys.IMenuService;

@Service(interfaceClass = IKhRoleManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "客户角色")
public class KhRoleManager extends RoleManager<KhRole, KhUser, IKhRoleService, KhCompany, IKhCompanyService>
		implements IKhRoleManager {

	@Autowired
	private IKhRoleService khRoleService;

	@Autowired
	private IMenuService menuService;

	@Autowired
	private IKhCompanyService khCompanyService;

	@Autowired
	private IApplicationService applicationService;

	@Override
	protected IKhRoleService service() {
		return khRoleService;
	}

	@Override
	protected IKhCompanyService companyService() {
		return khCompanyService;
	}

	@Override
	protected List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId) {
		if (companyId == null || companyId.longValue() <= 0) {
			return menuService.getValidMenus(t, appId);
		} else {
			return khCompanyService.findCompanyValidMenus(t, companyId, appId);
		}
	}

	@Override
	protected List<Tree> findCompanyValidOrgs(Trace t, Long companyId) {
		return khCompanyService.findCompanyValidOrgResource(t, companyId);
	}

//    @Override
//    public Long getRoleAreaRootId(Trace t, Long roleId, Long appId) {
//        // Long[] permedItemIds = khRoleService.findPermedAreaIds(t, roleId, appId);
//        // if (permedItemIds != null && permedItemIds.length > 0) {
//        // Area area = areaService.fetchById(t, permedItemIds[0]);
//        // return area.getParentId();
//        // }
//
//        KhRole role = service().fetchById(t, roleId);
//        return khCompanyService.getCompanyAreaRootId(t, role.getCompanyId(), appId);
//    }

	@Override
	public Long getCompanyAreaRootId4Role(Trace t, Long companyId) {
		Application app = applicationService.fetchByCode(t, "lac_app_um_kh");
		return khCompanyService.getCompanyAreaRootId(t, companyId, app.getId());
	}

//    @Override
//    public PermedAreaVo findRoleValidAreaResource(Trace t, Long roleId, Long appId) {
//        KhRole role = service().fetchById(t, roleId);
//        return khCompanyService.findCompanyValidAreaResource(t, role.getCompanyId(), appId);
//    }

	@Override
	public PermedAreaVo findCompanyValidAreaResource4Role(Trace t, Long companyId) {
		Application app = applicationService.fetchByCode(t, "lac_app_um_kh");
		return khCompanyService.findCompanyValidAreaResource(t, companyId, app.getId());
	}

}
