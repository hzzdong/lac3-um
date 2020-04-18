package com.linkallcloud.um.server.manager.party;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwRoleService;
import com.linkallcloud.um.service.sys.IApplicationService;
import com.linkallcloud.um.service.sys.IMenuService;

@Service(interfaceClass = IYwRoleManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "角色")
public class YwRoleManager extends RoleManager<YwRole, YwUser, IYwRoleService, YwCompany, IYwCompanyService>
		implements IYwRoleManager {

	@Autowired
	private IYwRoleService ywRoleService;

	@Autowired
	private IMenuService menuService;

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Autowired
	private IApplicationService applicationService;

	@Override
	protected IYwRoleService service() {
		return ywRoleService;
	}

	@Override
	protected IYwCompanyService companyService() {
		return ywCompanyService;
	}

	@Override
	protected List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId) {
		if (companyId == null || companyId.longValue() <= 0) {
			return menuService.getValidMenusWithButton(t, appId);
		} else {
			return ywCompanyService.findCompanyValidMenus(t, companyId, appId);
		}
	}

	@Override
	protected List<Tree> findCompanyValidOrgs(Trace t, Long companyId) {
		return ywCompanyService.findCompanyValidOrgResource(t, companyId);
	}

//	@Override
//	public Long getRoleAreaRootId(Trace t, Long roleId, Long appId) {
//		YwRole role = service().fetchById(t, roleId);
//		return ywCompanyService.getCompanyAreaRootId(t, role.getCompanyId(), appId);
//	}

	@Override
	public Long getCompanyAreaRootId4Role(Trace t, Long companyId) {
		Application app = applicationService.fetchByCode(t, "lac_app_um");
		return ywCompanyService.getCompanyAreaRootId(t, companyId, app.getId());
	}

//    @Override
//    public PermedAreaVo findRoleValidAreaResource(Trace t, Long roleId, Long appId) {
//        YwRole role = service().fetchById(t, roleId);
//        return ywCompanyService.findCompanyValidAreaResource(t, role.getCompanyId(), appId);
//    }

	@Override
	public PermedAreaVo findCompanyValidAreaResource4Role(Trace t, Long companyId) {
		Application app = applicationService.fetchByCode(t, "lac_app_um");
		return ywCompanyService.findCompanyValidAreaResource(t, companyId, app.getId());
	}

}
