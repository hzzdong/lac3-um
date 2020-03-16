package com.linkallcloud.um.pc.controller.sys;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.iapi.party.IYwRoleManager;

@Controller
@RequestMapping(value = "/YwPerm", method = RequestMethod.POST)
@Module(name = "权限")
public class YwPermController {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwRoleManager ywRoleManager;

	@RequestMapping(value = "/setup", method = RequestMethod.GET)
	public String setup(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap) {
		if (roleId != null && roleUuid != null) {
			YwRole role = ywRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		Query query = new Query();
		query.addRule(new Equal("status", 0));
		List<YwRole> roles = ywRoleManager.find(t, query);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/party/YwPerm/setup";
	}

}
