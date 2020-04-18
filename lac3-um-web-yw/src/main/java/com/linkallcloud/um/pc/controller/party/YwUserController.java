package com.linkallcloud.um.pc.controller.party;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.iapi.party.IOrgManager;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.um.pc.oapi.dd.ZzdUserOapi;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping(value = "/YwUser", method = RequestMethod.POST)
@Module(name = "用户")
public class YwUserController
		extends UserController<YwUser, IYwUserManager, YwRole, IYwRoleManager, Org, IOrgManager<Org>> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwRoleManager ywRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwDepartmentManager ywDepartmentManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwSystemConfigManager ywSystemConfigManager;

	@Autowired
	private ZzdUserOapi zzdUserOapi;

	@Override
	protected IYwUserManager manager() {
		return ywUserManager;
	}

	@Override
	protected IYwCompanyManager getCompanyManager() {
		return ywCompanyManager;
	}

	@Override
	protected IYwRoleManager getRoleManager() {
		return ywRoleManager;
	}

	@Override
	protected IYwDepartmentManager getDepartmentManager() {
		return ywDepartmentManager;
	}

	@Override
	protected String doMain4Parent(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
			AppVisitor av) {
		YwSystemConfig cfg = ywSystemConfigManager.fetchByCompanyId(t, av.companyId());
		modelMap.put("cfg", cfg == null ? new YwSystemConfig() : cfg);

		return super.doMain4Parent(prepare, parentId, parentClass, t, modelMap, av);
	}

	@Override
	protected YwUser doSave(YwUser user, Trace t, AppVisitor av) {
		user.setAccount(user.getAccount().trim());

		YwUser dbEntity = null;
		if (user.getId() != null && user.getUuid() != null) {
			dbEntity = manager().fetchById(t, user.getId());
		}

		YwUser ret = super.doSave(user, t, av);

		if (dbEntity != null) {
			String oldDduid = dbEntity.getDduid();
			int oldDdStatus = dbEntity.getDdStatus();
			String oldMobile = dbEntity.getMobile();
			String newMobile = user.getMobile();
			if (!Strings.isBlank(newMobile) && !newMobile.equals(oldMobile)) {// 更改了手机号码
				manager().updateDduid(t, user.getId(), "", 0);

				if (!Strings.isBlank(oldMobile) && oldDdStatus == 1 && !Strings.isBlank(oldDduid)) {// 原号码进行离职
					String accessToken = zzdUserOapi.getAccessToken();
					zzdUserOapi.disassociate(accessToken, Long.parseLong(oldDduid));
				}
			}
		}
		return ret;
	}

	@Override
	protected Page<YwUser> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
		Page<YwUser> page = webPage.toPage();

		if (!page.hasRule4Field("companyId")) {
			page.addRule(new Equal("companyId", av.companyId()));
		}

		if (page.hasRule4Field("parentId")) {// 查部门下的人
			return manager().findPage(t, page);
		} else {// 查整个公司的人
			if (av.isAdmin()) {
				return manager().findPage(t, page);
			} else {
				page.addRule(new Equal("appId", av.appId()));
				page.addRule(new Equal("userId", av.id()));
				return manager().findPermedUserPage(t, page);
			}
		}
	}

	@RequestMapping(value = "/list4Role", method = RequestMethod.GET)
	public String list4Role(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
			SessionUser su) {
		if (roleId != null && roleUuid != null) {
			YwRole role = ywRoleManager.fetchByIdUuid(t, roleId, roleUuid);
			modelMap.put("roleName", role.getName());
		}

		List<YwRole> roles = ywRoleManager.findCompanyAllRoles(t, su.companyId());
		modelMap.put("roles", roles);

		modelMap.put("roleId", roleId);
		modelMap.put("roleUuid", roleUuid);
		return "page/party/YwUser/list4Role";
	}

	@RequestMapping(value = "/page4Role", method = RequestMethod.GET)
	public @ResponseBody Result<Page<YwUser>> page4Role(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<YwUser> page = webPage.toPage();
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long companyId = av.companyId();
		Equal r = (Equal) page.getRule4Field("companyId");
		if (r != null) {
			r.setValue(companyId);
		} else {
			r = new Equal("companyId", companyId);
			page.addRule(r);
		}

		Equal rr = (Equal) page.getRule4Field("roleId");
		if (rr != null && rr.getValue() != null) {
			YwRole role = getRoleManager().fetchById(t, (Long) rr.getValue());
			page.addRule(new Equal("type", role.getType()));
		}

		page = manager().findPage4Role(t, page);
		return new Result<>(page);
	}

	// list4SysRole
	@RequestMapping(value = "/list4SysRole", method = RequestMethod.GET)
	public String list4SysRole(@RequestParam(value = "roleId", required = false) Long roleId,
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
		return "page/party/YwUser/list4SysRole";
	}

	// page4SysRole
	@RequestMapping(value = "/page4SysRole", method = RequestMethod.GET)
	public @ResponseBody Result<Page<YwUser>> page4SysRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
			throws IllegalParameterException {
		Page<YwUser> page = webPage.toPage();
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
		Boolean ret = ywUserManager.addUserRoles(t, userId, userUuid, roleUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeRoles", method = RequestMethod.GET)
	public @ResponseBody Result<Object> removeRoles(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "userUuid", required = false) String userUuid,
			@RequestBody Map<String, Long> roleUuidIds, Trace t, ModelMap modelMap) {
		Boolean ret = ywUserManager.removeUserRoles(t, userId, userUuid, roleUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/associate", method = RequestMethod.POST)
	public @ResponseBody Result<Object> associate(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		YwUser ywUser = ywUserManager.fetchByIdUuid(t, id, uuid);
		if (ywUser != null) {
			String mobile = ywUser.getMobile();
			if (!StringUtils.isBlank(mobile) && mobile.length() == 11) {// 判断手机号码存在
				String accessToken = zzdUserOapi.getAccessToken();
				Long dduid = zzdUserOapi.getSingleUserIdByMobile(accessToken, mobile);
				if (dduid != null) {
					boolean ret = zzdUserOapi.associate(accessToken, dduid);
					if (ret) {
						// 修改ddStatus为1,同步成功
						ywUserManager.updateDduid(t, ywUser.getId(), dduid.toString(), 1);
						// 清除除userId外的所有手机号码为mobile的用户的手机号码为空
						ywUserManager.cleanOtherUserMobileByUserId(t, ywUser.getMobile(), ywUser.getId());
					} else {
						// 修改ddStatus为2,同步成功
						ywUserManager.updateDduid(t, ywUser.getId(), dduid.toString(), 2);
					}
					return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "兼职到浙政钉虚拟目录失败");
				} else {
					// 修改ddStatus为2,同步成功,浙政钉用户不存在
					ywUserManager.updateDduid(t, ywUser.getId(), "", 2);
					return new Result<Object>(Exceptions.CODE_ERROR_UNKNOW,
							"浙政钉用户不存在，请确认您的手机号(" + mobile + ")已经在浙政钉开户成功。");
				}
			} else {
				return new Result<Object>(Exceptions.CODE_ERROR_UNKNOW, "手机号码为空或者手机号有误");
			}
		}

		return new Result<Object>(Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/disassociate", method = RequestMethod.POST)
	public @ResponseBody Result<Object> disassociate(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		boolean ret = false;
		YwUser ywUser = ywUserManager.fetchByIdUuid(t, id, uuid);
		if (ywUser != null && !Strings.isBlank(ywUser.getDduid()) && ywUser.getDdStatus() == 1) {
			String accessToken = zzdUserOapi.getAccessToken();
			ret = zzdUserOapi.disassociate(accessToken, Long.parseLong(ywUser.getDduid()));
			if (ret) {
				ywUserManager.updateDduid(t, ywUser.getId(), "", 0);
			}
		}
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "从浙政钉虚拟目录离职失败");
	}
}
