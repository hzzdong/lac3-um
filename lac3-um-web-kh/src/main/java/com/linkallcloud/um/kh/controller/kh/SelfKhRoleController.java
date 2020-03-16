package com.linkallcloud.um.kh.controller.kh;

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
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;
import com.linkallcloud.um.kh.controller.party.RoleController;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.core.www.ISessionUser;

@Controller
@RequestMapping(value = "/SelfKhRole", method = RequestMethod.POST)
@Module(name = "角色")
public class SelfKhRoleController extends RoleController<KhRole, KhUser, IKhRoleManager, IKhUserManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhRoleManager khRoleManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhUserManager khUserManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhCompanyManager khCompanyManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhSystemConfigManager khSystemConfigManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAreaManager areaManager;

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
        return Domains.ROLE_NORMAL;
    }

    @Override
    protected String getMainPage() {
        return "page/khself/KhRole/list";
    }

    @Override
    protected String getEditPage() {
        return "page/khself/KhRole/edit";
    }

    @Override
    protected String getSelectPage() {
        return "page/khself/KhRole/select";
    }

    @RequestMapping(value = "/addApps", method = RequestMethod.GET)
    public @ResponseBody Boolean addApps(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid,
            @RequestBody Map<String, Long> appUuidIds, Trace t, ModelMap modelMap) {
        return manager().addRoleApps(t, roleId, roleUuid, appUuidIds);
    }

    @RequestMapping(value = "/removeApps", method = RequestMethod.GET)
    public @ResponseBody Boolean deleteApps(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid,
            @RequestBody Map<String, Long> appUuidIds, Trace t, ModelMap modelMap) {
        return manager().removeRoleApps(t, roleId, roleUuid, appUuidIds);
    }

    @RequestMapping(value = "/addUsers", method = RequestMethod.GET)
    public @ResponseBody Boolean addUsers(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid,
            @RequestBody Map<String, Long> userUuidIds, Trace t, ModelMap modelMap) {
        return manager().addRoleUsers(t, roleId, roleUuid, userUuidIds);
    }

    @RequestMapping(value = "/removeUsers", method = RequestMethod.GET)
    public @ResponseBody Boolean removeUsers(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid,
            @RequestBody Map<String, Long> userUuidIds, Trace t, ModelMap modelMap) {
        return manager().removeRoleUsers(t, roleId, roleUuid, userUuidIds);
    }

    @RequestMapping(value = "/permSetup", method = RequestMethod.GET)
    public String permSetup(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap,
            AppVisitor av) {
        KhSystemConfig sc = getKhSystemConfig(t, Long.parseLong(av.getCompanyId()));
        modelMap.put("sc", sc);

        if (roleId != null && roleUuid != null) {
            KhRole role = manager().fetchByIdUuid(t, roleId, roleUuid);
            modelMap.put("roleName", role.getName());
        }

        ISessionUser su = Controllers.getSessionUser();
        List<KhRole> roles = manager().findCompanyRoles(t, Long.parseLong(su.getCompanyId()), Domains.ROLE_NORMAL);
        modelMap.put("roles", roles);

        modelMap.put("roleId", roleId == null ? 0L : roleId);
        modelMap.put("roleUuid", roleUuid);

        return "page/khself/KhRole/permSetup";
    }

    private KhSystemConfig getKhSystemConfig(Trace t, Long companyId) {
        KhCompany company = khCompanyManager.fetchById(t, companyId);
        KhSystemConfig sc = khSystemConfigManager.fetchByCompanyId(t, company.rootCompanyId());
        if (sc == null) {
            sc = new KhSystemConfig();
        }

        String areaName = "";
        if (sc != null && sc.getRootAreaId() != null && sc.getRootAreaId().longValue() > 0) {
            Area area = areaManager.fetchById(t, sc.getRootAreaId());
            areaName = area.getName();
        } else {
            areaName = "中华人民共和国";
        }
        sc.setRootAreaName(areaName);
        return sc;
    }

    @RequestMapping(value = "/getPermedMenuTree", method = RequestMethod.GET)
    public @ResponseBody List<Tree> getUmTree(@RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "appUuid") String appUuid, Trace t) throws IllegalParameterException {
        ISessionUser su = Controllers.getSessionUser();
        List<Tree> items = manager().findPermedMenus(t, Long.parseLong(su.getCompanyId()), roleId, appId);
        return items;
    }

    @RequestMapping(value = "/saveRoleAppMenuPerm", method = RequestMethod.GET)
    public @ResponseBody Boolean saveRoleAppMenuPerms(@RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
            ModelMap modelMap) {
        return manager().saveRoleAppMenuPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
    }

    @RequestMapping(value = "/getPermedOrgTree", method = RequestMethod.GET)
    public @ResponseBody List<Tree> getPermedOrgTree(@RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "appUuid") String appUuid, Trace t) throws IllegalParameterException {
        ISessionUser su = Controllers.getSessionUser();
        List<Tree> items = manager().findPermedOrgs(t, Long.parseLong(su.getCompanyId()), roleId, appId);
        return items;
    }

    @RequestMapping(value = "/saveRoleAppOrgPerm", method = RequestMethod.GET)
    public @ResponseBody Boolean saveRoleAppOrgPerm(@RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
            ModelMap modelMap) {
        return manager().saveRoleAppOrgPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
    }

    @RequestMapping(value = "/getPermedAreaTree", method = RequestMethod.GET)
    public @ResponseBody PermedAreaVo getPermedAreaTree(@RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "appUuid") String appUuid,
            @RequestParam(value = "parentAreaId", required = false) Long parentAreaId, Trace t)
            throws IllegalParameterException {
        ISessionUser su = Controllers.getSessionUser();
        return manager().findPermedRoleAppAreas(t, parentAreaId, Long.parseLong(su.getCompanyId()), roleId, appId);
    }

    @RequestMapping(value = "/saveRoleAppAreaPerm", method = RequestMethod.GET)
    public @ResponseBody Boolean saveRoleAppAreaPerm(@RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleUuid") String roleUuid, @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
            ModelMap modelMap) {
        return manager().saveRoleAppAreaPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
    }

    @RequestMapping(value = "/list4User", method = RequestMethod.GET)
    public String list4User(@RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userUuid", required = false) String userUuid, Trace t, ModelMap modelMap) {
        if (userId != null && userUuid != null) {
            KhUser user = userManager().fetchByIdUuid(t, userId, userUuid);
            modelMap.put("userName", user.getName());
        }

        modelMap.put("userId", userId);
        modelMap.put("userUuid", userUuid);
        return "page/khself/KhRole/list4User";
    }

    @RequestMapping(value = "/page4User", method = RequestMethod.GET)
    public @ResponseBody Page<KhRole> page4User(@RequestBody WebPage webPage, Trace t, AppVisitor av)
            throws IllegalParameterException {
        Page<KhRole> page = webPage.toPage();
        if (!page.hasRule4Field("userId") || !page.hasRule4Field("userUuid")) {
            throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "userId,userUuid参数错误。");
        }
        return manager().findPage4User(t, page);
    }

}
