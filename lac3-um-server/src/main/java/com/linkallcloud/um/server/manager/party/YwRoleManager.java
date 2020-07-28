package com.linkallcloud.um.server.manager.party;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwRoleService;

@DubboService(interfaceClass = IYwRoleManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "角色")
public class YwRoleManager extends RoleManager<YwRole, YwUser, IYwRoleService, YwCompany, IYwCompanyService>
		implements IYwRoleManager {

	@Autowired
	private IYwRoleService ywRoleService;

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Override
	protected IYwRoleService service() {
		return ywRoleService;
	}

	@Override
	protected IYwCompanyService companyService() {
		return ywCompanyService;
	}

}
