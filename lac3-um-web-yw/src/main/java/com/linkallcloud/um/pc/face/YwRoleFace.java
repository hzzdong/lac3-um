package com.linkallcloud.um.pc.face;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;

@Controller
@RequestMapping(value = "/face/YwRole", method = RequestMethod.POST)
@Module(name = "运维角色")
public class YwRoleFace extends
		RoleBaseFace<YwRole, YwUser, IYwRoleManager, IYwUserManager, YwCompany, IYwCompanyManager, YwDepartment, IYwDepartmentManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwRoleManager ywRoleManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwDepartmentManager ywDepartmentManager;

	public YwRoleFace() {
		super();
	}

	@Override
	protected IYwRoleManager manager() {
		return ywRoleManager;
	}

	@Override
	protected IYwCompanyManager companyManager() {
		return ywCompanyManager;
	}

	@Override
	protected IYwDepartmentManager departmentManager() {
		return ywDepartmentManager;
	}

	@Override
	protected IYwUserManager userManager() {
		return ywUserManager;
	}

	@Override
	protected int getRoleType() {
		return Domains.ROLE_NORMAL;
	}

}
