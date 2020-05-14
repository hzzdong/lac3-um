package com.linkallcloud.um.pc.controller.party;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/YwRole", method = RequestMethod.POST)
@Module(name = "角色")
public class YwRoleController extends RoleController<YwRole, YwUser, IYwRoleManager, IYwUserManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwRoleManager ywRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwSystemConfigManager ywSystemConfigManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Override
	protected IYwRoleManager manager() {
		return ywRoleManager;
	}

	@Override
	protected IYwUserManager userManager() {
		return ywUserManager;
	}

	@Override
	protected int getRoleType() {
		return Domains.ROLE_NORMAL;
	}

	@RequestMapping(value = "/addApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addApps(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> appUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = ywRoleManager.addRoleApps(t, roleId, roleUuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> deleteApps(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> appUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = ywRoleManager.removeRoleApps(t, roleId, roleUuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/addUsers", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addUsers(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> userUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = ywRoleManager.addRoleUsers(t, roleId, roleUuid, userUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeUsers", method = RequestMethod.GET)
	public @ResponseBody Result<Object> removeUsers(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestBody Map<String, Long> userUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = ywRoleManager.removeRoleUsers(t, roleId, roleUuid, userUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/getPermedMenuTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> getUmTree(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, Trace t) throws IllegalParameterException {
		List<Tree> items = ywRoleManager.findPermedMenus(t, null, roleId, appId);
		return new Result<>(items);
	}

	@RequestMapping(value = "/permSetup", method = RequestMethod.GET)
	public String permSetup(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
			AppVisitor av) {
		YwSystemConfig sc = getYwSystemConfig(t, av.companyId());
		modelMap.put("sc", sc);

		if (roleId != null && roleUuid != null) {
			YwRole role = ywRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		List<YwRole> roles = ywRoleManager.findCompanyRoles(t, av.companyId(), Domains.ROLE_NORMAL);
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);

		return "page/party/YwRole/permSetup";
	}

	private YwSystemConfig getYwSystemConfig(Trace t, Long companyId) {
		YwCompany company = ywCompanyManager.fetchById(t, companyId);
//		YwSystemConfig sc = ywSystemConfigManager.fetchByCompanyId(t, company.rootCompanyId());
//		if (sc == null) {
//			sc = new YwSystemConfig();
//		}
//
//		String areaName = "";
//		if (sc != null && sc.getRootAreaId() != null && sc.getRootAreaId().longValue() > 0) {
//			Area area = areaManager.fetchById(t, sc.getRootAreaId());
//			areaName = area.getName();
//		} else {
//			areaName = "中华人民共和国";
//		}
//		sc.setRootAreaName(areaName);
//		return sc;
		return null;
	}

	@RequestMapping(value = "/saveRoleAppMenuPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveRoleAppMenuPerms(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = ywRoleManager.saveRoleAppMenuPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/getPermedOrgTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> getPermedOrgTree(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, Trace t,SessionUser su) throws IllegalParameterException {
		List<Tree> items = manager().findPermedOrgs(t, su.companyId(), roleId, appId);
		return new Result<>(items);
	}

	@RequestMapping(value = "/saveRoleAppOrgPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveRoleAppOrgPerm(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = manager().saveRoleAppOrgPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/getPermedAreaTree", method = RequestMethod.GET)
	public @ResponseBody Result<PermedAreaVo> getPermedAreaTree(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid,
			@RequestParam(value = "parentAreaId", required = false) Long parentAreaId, Trace t,SessionUser su)
			throws IllegalParameterException {
		PermedAreaVo ret = manager().findPermedRoleAppAreas(t, parentAreaId, su.companyId(), roleId,
				appId);
		return new Result<PermedAreaVo>(ret);
	}

	@RequestMapping(value = "/saveRoleAppAreaPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveRoleAppAreaPerm(@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = manager().saveRoleAppAreaPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/list4User", method = RequestMethod.GET)
	public String list4User(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "userUuid", required = false) String userUuid, Trace t, ModelMap modelMap) {
		if (userId != null && userUuid != null) {
			YwUser user = ywUserManager.fetchByIdUuid(t, userId, userUuid);
			modelMap.put("userName", user.getName());
		}

		modelMap.put("userId", userId);
		modelMap.put("userUuid", userUuid);
		return "page/party/YwRole/list4User";
	}

	@RequestMapping(value = "/page4User", method = RequestMethod.GET)
	public @ResponseBody Result<Page<YwRole>> page4User(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<YwRole> page = webPage.toPage();
		if (!page.hasRule4Field("userId") || !page.hasRule4Field("userUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "userId,userUuid参数错误。");
		}
		page = manager().findPage4User(t, page);
		return new Result<Page<YwRole>>(page);
	}

}
