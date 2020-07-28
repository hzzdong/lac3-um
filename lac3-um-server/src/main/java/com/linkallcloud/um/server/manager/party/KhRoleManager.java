package com.linkallcloud.um.server.manager.party;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.party.IKhRoleService;

@DubboService(interfaceClass = IKhRoleManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "客户角色")
public class KhRoleManager extends RoleManager<KhRole, KhUser, IKhRoleService, KhCompany, IKhCompanyService>
		implements IKhRoleManager {

	@Autowired
	private IKhRoleService khRoleService;

	@Autowired
	private IKhCompanyService khCompanyService;

	@Override
	protected IKhRoleService service() {
		return khRoleService;
	}

	@Override
	protected IKhCompanyService companyService() {
		return khCompanyService;
	}

	@Override
	public Tree findPermedMenuTree4SysKhRole(Trace t, Long roleId, Long appId) {
		Tree tree = companyService().loadCompanyMenuTree(t, null, appId);
		Long[] permedItemIds = service().findPermedMenuIds(t, roleId, appId);
		Trees.checked(tree, permedItemIds);
		return tree;
	}

}
