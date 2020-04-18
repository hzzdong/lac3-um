package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;

@Controller
@RequestMapping(value = "/face/KhRole", method = RequestMethod.POST)
@Module(name = "客户角色")
public class KhRoleFace extends
		RoleBaseFace<KhRole, KhUser, IKhRoleManager, IKhUserManager, KhCompany, IKhCompanyManager, KhDepartment, IKhDepartmentManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhDepartmentManager khDepartmentManager;

	public KhRoleFace() {
		super();
	}

	@Override
	protected IKhRoleManager manager() {
		return khRoleManager;
	}

	@Override
	protected IKhCompanyManager companyManager() {
		return khCompanyManager;
	}

	@Override
	protected IKhDepartmentManager departmentManager() {
		return khDepartmentManager;
	}

	@Override
	protected IKhUserManager userManager() {
		return khUserManager;
	}

	@Override
	protected int getRoleType() {
		return Domains.ROLE_NORMAL;
	}

}
