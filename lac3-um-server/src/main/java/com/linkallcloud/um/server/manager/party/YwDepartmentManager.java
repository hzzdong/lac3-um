package com.linkallcloud.um.server.manager.party;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwUserService;

@Service(interfaceClass = IYwDepartmentManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "运维部门")
public class YwDepartmentManager extends
		DepartmentManager<YwDepartment, IYwDepartmentService, YwUser, IYwUserService, YwCompany, IYwCompanyService>
		implements IYwDepartmentManager {

	@Autowired
	private IYwUserService ywUserService;

	@Autowired
	private IYwDepartmentService ywDepartmentService;

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Override
	protected IYwDepartmentService service() {
		return ywDepartmentService;
	}

	@Override
	protected IYwUserService userService() {
		return ywUserService;
	}

	@Override
	protected IYwCompanyService companyService() {
		return ywCompanyService;
	}

}
