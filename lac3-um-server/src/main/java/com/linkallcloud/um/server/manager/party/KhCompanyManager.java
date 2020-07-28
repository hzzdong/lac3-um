package com.linkallcloud.um.server.manager.party;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.party.IKhDepartmentService;
import com.linkallcloud.um.service.party.IKhRoleService;
import com.linkallcloud.um.service.party.IKhUserService;

@DubboService(interfaceClass = IKhCompanyManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "客户单位")
public class KhCompanyManager extends
		CompanyManager<KhCompany, IKhCompanyService, KhDepartment, IKhDepartmentService, KhUser, IKhUserService, KhRole, IKhRoleService>
		implements IKhCompanyManager {

	@Autowired
	private IKhCompanyService khCompanyService;

	@Autowired
	private IKhUserService khUserService;

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

}
