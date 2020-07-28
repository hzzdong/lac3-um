package com.linkallcloud.um.server.manager.party;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwRoleService;
import com.linkallcloud.um.service.party.IYwUserService;

@DubboService(interfaceClass = IYwCompanyManager.class, version = "${dubbo.service.version}")
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

}
