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
import com.linkallcloud.web.controller.BaseLController4ParentTree;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.core.www.ISessionUser;

@Controller
@RequestMapping(value = "/KhUser", method = RequestMethod.POST)
@Module(name = "用户")
// @RequirePermissions({ "customer_qy-user" })
public class KhUserController extends BaseLController4ParentTree<KhUser, IKhUserManager, KhCompany, IKhCompanyManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Override
	protected IKhCompanyManager parentManager(String parentClass) {
		return khCompanyManager;
	}

	@Override
	protected IKhUserManager manager() {
		return khUserManager;
	}

	@Override
	protected String getMainPage() {
		return "page/party/KhUser/main";
	}

	@Override
	protected String getEditPage() {
		return "page/party/KhUser/edit";
	}

	@Override
	protected String getSelectPage() {
		return "page/party/KhUser/select";
	}

	@Override
	protected Page<KhUser> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
		Page<KhUser> page = webPage.toPage();
		page.addRule(new Equal("ywUserId", Long.parseLong(av.getId())));
		page.addRule(new Equal("appId", Long.parseLong(av.getAppId())));
		return manager().findPage(t, page);
	}

	@Override
	protected Page<KhUser> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
		Page<KhUser> page = webPage.toPage();
		page.addRule(new Equal("ywUserId", Long.parseLong(av.getId())));
		page.addRule(new Equal("appId", Long.parseLong(av.getAppId())));
		return manager().findPage4Select(t, page);
	}

	@Override
	protected KhUser doGet4Parent(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		KhUser entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = manager().fetchByIdUuid(t, id, uuid);
		} else {
			entity = mirror.born();
			entity.setParentId(parentId);
			entity.setParentClass(parentClass);
		}

		if (entity.getCompanyId() != null && entity.getCompanyId().longValue() > 0) {
			KhCompany company = parentManager(parentClass).fetchById(t, entity.getCompanyId());
			if (company != null) {
				entity.setOrgName(company.getName());
			}
		}
		return entity;
	}

	// list4SysRole
	@RequestMapping(value = "/list4SysRole", method = RequestMethod.GET)
	public String list4SysRole(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap) {
		if (roleId != null && roleUuid != null) {
			KhRole role = khRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		ISessionUser su = Controllers.getSessionUser();
		List<KhRole> roles = khRoleManager.findCompanyRoles(t, Long.parseLong(su.getCompanyId()), Domains.ROLE_SYSTEM);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/party/KhUser/list4SysRole";
	}

	// page4SysRole
	@RequestMapping(value = "/page4SysRole", method = RequestMethod.GET)
	public @ResponseBody Result<Page<KhUser>> page4SysRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<KhUser> page = webPage.toPage();
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		page = manager().findPage4Role(t, page);
		return new Result<>(page);
	}

	@RequestMapping(value = "/addRoles", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addRoles(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "userUuid", required = false) String userUuid,
			@RequestBody Map<String, Long> roleUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = khUserManager.addUserRoles(t, userId, userUuid, roleUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeRoles", method = RequestMethod.GET)
	public @ResponseBody Result<Object> removeRoles(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "userUuid", required = false) String userUuid,
			@RequestBody Map<String, Long> roleUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = khUserManager.removeUserRoles(t, userId, userUuid, roleUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

}
