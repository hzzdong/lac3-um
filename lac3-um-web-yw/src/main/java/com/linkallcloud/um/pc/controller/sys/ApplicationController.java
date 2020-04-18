package com.linkallcloud.um.pc.controller.sys;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.web.controller.BaseLController;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping(value = "/Application", method = RequestMethod.POST)
@Module(name = "应用")
public class ApplicationController extends BaseLController<Application, IApplicationManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwRoleManager ywRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Override
	protected IApplicationManager manager() {
		return applicationManager;
	}

	@Override
	protected String doEdit(boolean prepare, Long parentId, String parentClass, Long id, String uuid, Trace t,
			ModelMap modelMap, AppVisitor av) {
		Application app = manager().fetchByIdUuid(t, id, uuid);
		modelMap.put("app", app);
		return super.doEdit(true, parentId, parentClass, id, uuid, t, modelMap, av);
	}

	@RequestMapping(value = { "/saveInter" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@WebLog(db = true)
	@ResponseBody
	public Result<Object> saveInter(@RequestBody @Valid Application entity, Trace t, AppVisitor av) {
		try {
			Application dbApp = manager().fetchByIdUuid(t, entity.getId(), entity.getUuid());
			dbApp.setHost(entity.getHost());
			dbApp.setTimeout(entity.getTimeout());
			dbApp.setMessageEncAlg(entity.getMessageEncAlg());
			dbApp.setMessageEncKey(entity.getMessageEncKey());
			dbApp.setSignatureAlg(entity.getSignatureAlg());
			dbApp.setSignatureKey(entity.getSignatureKey());
			manager().updateInterfaceInfo(t, dbApp);
			return new Result<>(dbApp);
		} catch (Throwable e) {
			return Exceptions.makeErrorResult(e);
		}
	}

	@RequestMapping(value = { "/saveMappingInter" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@WebLog(db = true)
	@ResponseBody
	public Result<Object> saveMappingInter(@RequestBody @Valid Application entity, Trace t, AppVisitor av) {
		try {
			Application dbApp = manager().fetchByIdUuid(t, entity.getId(), entity.getUuid());
			dbApp.setAuthAddr(entity.getAuthAddr());
			dbApp.setAuthPassMode(entity.getAuthPassMode());
			dbApp.setAuthSignAlg(entity.getAuthSignAlg());
			dbApp.setAuthSignKey(entity.getAuthSignKey());
			manager().updateMappingInfo(t, dbApp);
			return new Result<>(dbApp);
		} catch (Throwable e) {
			return Exceptions.makeErrorResult(e);
		}
	}

	@RequestMapping(value = "/list4KhCompany", method = RequestMethod.GET)
	public String list4KhCompany(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap, AppVisitor av) {
		if (id != null && uuid != null) {
			KhCompany company = khCompanyManager.fetchByIdUuid(t, id, uuid);
			modelMap.put("khCompanyName", company.getName());
		}

		modelMap.put("companies", findApp4UserApp(t, av));

		modelMap.put("khCompanyId", id == null ? "" : id);
		modelMap.put("khCompanyUuid", uuid);
		return "page/Application/list4KhCompany";
	}

	private List<KhCompany> findApp4UserApp(Trace t, AppVisitor av) {
		Query query = new Query();
		query.addRule(new Equal("ywUserId", av.id()));
		query.addRule(new Equal("appId", av.appId()));
		List<KhCompany> companies = khCompanyManager.find(t, query);
		return companies;
	}

	@RequestMapping(value = "/page4KhCompany", method = RequestMethod.GET)
	public @ResponseBody Result<Page<Application>> page4KhCompany(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<Application> page = webPage.toPage();
		if (!page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "khCompanyId,khCompanyUuid参数错误。");
		}

		page.addRule(new Equal("ywUserId", av.id()));
		page.addRule(new Equal("appId", av.appId()));

		page = manager().findPage4KhCompany(t, page);
		return new Result<>(page);
	}

	@RequestMapping(value = "/list4YwRole", method = RequestMethod.GET)
	public String list4YwRole(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
			SessionUser su) {
		if (roleId != null && roleUuid != null) {
			YwRole role = ywRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		List<YwRole> roles = ywRoleManager.findCompanyRoles(t, su.companyId(), Domains.ROLE_NORMAL);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/Application/list4YwRole";
	}

	@RequestMapping(value = "/page4YwRole", method = RequestMethod.GET)
	public @ResponseBody Result<Page<Application>> page4YwRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<Application> page = webPage.toPage();
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		page = manager().findPage4YwRole(t, page);
		return new Result<>(page);
	}

	// list4YwSysRole
	@RequestMapping(value = "/list4YwSysRole", method = RequestMethod.GET)
	public String list4YwSysRole(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
			SessionUser su) {
		if (roleId != null && roleUuid != null) {
			YwRole role = ywRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		List<YwRole> roles = ywRoleManager.findCompanyRoles(t, su.companyId(), Domains.ROLE_SYSTEM);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/Application/list4YwSysRole";
	}

	// page4YwSysRole
	@RequestMapping(value = "/page4YwSysRole", method = RequestMethod.GET)
	public @ResponseBody Result<Page<Application>> page4YwSysRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<Application> page = webPage.toPage();
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		page = manager().findPage4YwRole(t, page);
		return new Result<>(page);
	}

	// list4KhSysRole
	@RequestMapping(value = "/list4KhSysRole", method = RequestMethod.GET)
	public String list4KhSysRole(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
			SessionUser su) {
		if (roleId != null && roleUuid != null) {
			KhRole role = khRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		List<KhRole> roles = khRoleManager.findCompanyRoles(t, su.companyId(), Domains.ROLE_SYSTEM);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/Application/list4KhSysRole";
	}

	// page4KhSysRole
	@RequestMapping(value = "/page4KhSysRole", method = RequestMethod.GET)
	public @ResponseBody Result<Page<Application>> page4KhSysRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<Application> page = webPage.toPage();
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		page = manager().findPage4KhRole(t, page);
		return new Result<>(page);
	}

	@RequestMapping(value = "/list4SelfKhRole", method = RequestMethod.GET)
	public String list4SelfKhRole(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
			SessionUser su) {
		if (roleId != null && roleUuid != null) {
			KhRole role = khRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		modelMap.put("khCompanyId", su.companyId());

		List<KhRole> roles = khRoleManager.findCompanyRoles(t, su.companyId(), Domains.ROLE_NORMAL);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/Application/list4SelfKhRole";
	}

	@RequestMapping(value = "/page4SelfKhRole", method = RequestMethod.GET)
	public @ResponseBody Result<Page<Application>> page4SelfKhRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<Application> page = webPage.toPage();
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		page = manager().findPage4KhRole(t, page);
		return new Result<>(page);
	}

}
