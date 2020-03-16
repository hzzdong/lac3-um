package com.linkallcloud.um.pc.controller.party;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.core.util.Domains;

@Controller
@RequestMapping(value = "/KhSysRole", method = RequestMethod.POST)
@Module(name = "客户系统角色")
public class KhSysRoleController extends RoleController<KhRole, KhUser, IKhRoleManager, IKhUserManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Override
	protected IKhRoleManager manager() {
		return khRoleManager;
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
	protected String getMainPage() {
		return "page/party/KhSysRole/list";
	}

	@Override
	protected String getEditPage() {
		return "page/party/KhSysRole/edit";
	}

	@RequestMapping(value = "/addApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addApps(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> appUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = manager().addRoleApps(t, roleId, roleUuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> deleteApps(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> appUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = manager().removeRoleApps(t, roleId, roleUuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/addUsers", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addUsers(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> userUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = manager().addRoleUsers(t, roleId, roleUuid, userUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeUsers", method = RequestMethod.GET)
	public @ResponseBody Result<Object> removeUsers(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> userUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = manager().removeRoleUsers(t, roleId, roleUuid, userUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/getPermedMenuTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> getUmTree(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, Trace t) throws IllegalParameterException {
		List<Tree> items = manager().findPermedMenus(t, null, roleId, appId);
		return new Result<>(items);
	}

	@RequestMapping(value = "/permSetup", method = RequestMethod.GET)
	public String permSetup(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap) {
		if (roleId != null && roleUuid != null) {
			KhRole role = manager().fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		Query query = new Query();
		query.addRule(new Equal("status", 0));
		query.addRule(new Equal("type", Domains.ROLE_SYSTEM));
		List<KhRole> roles = manager().find(t, query);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);

		return "page/party/KhSysRole/permSetup";
	}

	@RequestMapping(value = "/saveRoleAppMenuPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveRoleAppMenuPerms(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = manager().saveRoleAppMenuPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

}
