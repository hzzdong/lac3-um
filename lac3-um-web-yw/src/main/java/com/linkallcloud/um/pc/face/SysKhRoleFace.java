package com.linkallcloud.um.pc.face;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.ParentIdFaceRequest;
import com.linkallcloud.core.face.message.request.RelFaceRequest;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/SysKhRole", method = RequestMethod.POST)
@Module(name = "客户系统角色")
public class SysKhRoleFace extends
		RoleBaseFace<KhRole, KhUser, IKhRoleManager, IKhUserManager, KhCompany, IKhCompanyManager, KhDepartment, IKhDepartmentManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhDepartmentManager khDepartmentManager;

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
		return Domains.ROLE_SYSTEM;
	}

	@Override
	protected IKhRoleManager manager() {
		return khRoleManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/getPermedMenuTree4SysKhRole", method = RequestMethod.POST)
	public @ResponseBody Object getPermedMenuTree4SysKhRole(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Tree tree = manager().findPermedMenuTree4SysKhRole(t, fr.getParentId(), fr.getId());
		return tree.getChildren();
	}

	@Override
	protected Boolean doAddRoleUsers(Trace t, RelFaceRequest fr, Long companyId) {
		if (fr.getUuidIds() != null && !fr.getUuidIds().isEmpty()) {
			Long userId = fr.getUuidIds().values().iterator().next();
			KhUser user = userManager().fetchById(t, userId);
			companyId = user.getCompanyId();
		}
		return super.doAddRoleUsers(t, fr, companyId);
	}

	@Override
	protected Boolean doRemoveRoleUsers(Trace t, ParentIdFaceRequest fr, Long companyId) {
		if (fr.getId() != null) {
			KhUser user = userManager().fetchById(t, fr.getId());
			companyId = user.getCompanyId();
		}
		return super.doRemoveRoleUsers(t, fr, companyId);
	}

}
