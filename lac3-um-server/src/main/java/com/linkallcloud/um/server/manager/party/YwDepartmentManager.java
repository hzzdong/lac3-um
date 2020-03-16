package com.linkallcloud.um.server.manager.party;

import com.linkallcloud.um.service.party.IYwDepartmentService;
import com.linkallcloud.um.service.party.IYwUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.Service;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;

@Service(interfaceClass = IYwDepartmentManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "运维部门")
public class YwDepartmentManager extends DepartmentManager<YwDepartment, IYwDepartmentService, YwUser, IYwUserService>
		implements IYwDepartmentManager {

	@Autowired
	private IYwUserService ywUserService;

	@Autowired
	private IYwDepartmentService ywDepartmentService;

	@Override
	protected IYwDepartmentService service() {
		return ywDepartmentService;
	}

	@Override
	protected IYwUserService userService() {
		return ywUserService;
	}

}
