package com.linkallcloud.um.server.manager.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.Service;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.um.server.service.party.IKhDepartmentService;
import com.linkallcloud.um.server.service.party.IKhUserService;

@Service(interfaceClass = IKhDepartmentManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "客户部门")
public class KhDepartmentManager extends DepartmentManager<KhDepartment, IKhDepartmentService, KhUser, IKhUserService>
		implements IKhDepartmentManager {

	@Autowired
	private IKhUserService khUserService;

	@Autowired
	private IKhDepartmentService khDepartmentService;

	@Override
	protected IKhDepartmentService service() {
		return khDepartmentService;
	}

	@Override
	protected IKhUserService userService() {
		return khUserService;
	}

}
