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
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.party.IOrgManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.kh.controller.party.UserController;
import com.linkallcloud.core.www.ISessionUser;

@Controller
@RequestMapping(value = "/SelfKhUser", method = RequestMethod.POST)
@Module(name = "用户")
public class SelfKhUserController
        extends UserController<KhUser, IKhUserManager, KhRole, IKhRoleManager, Org, IOrgManager<Org>> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhUserManager khUserManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhRoleManager khRoleManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhCompanyManager khCompanyManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhDepartmentManager khDepartmentManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IApplicationManager applicationManager;

    @Override
    protected IKhUserManager manager() {
        return khUserManager;
    }

    @Override
    protected IKhRoleManager getRoleManager() {
        return khRoleManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected IKhCompanyManager getCompanyManager() {
        return khCompanyManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected IKhDepartmentManager getDepartmentManager() {
        return khDepartmentManager;
    }

    @Override
    protected String getMainPage() {
        return "page/khself/KhUser/main";
    }

    @Override
    protected String getEditPage() {
        return "page/khself/KhUser/edit";
    }

    @Override
    protected String getSelectPage() {
        return "page/khself/KhUser/select";
    }

    @Override
    protected Page<KhUser> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
        ISessionUser su = Controllers.getSessionUser();
        Page<KhUser> page = webPage.toPage();
        if (!page.hasRule4Field("companyId")) {
            page.addRule(new Equal("companyId", Long.valueOf(su.getCompanyId())));
        }

        if (page.hasRule4Field("parentId")) {// 查部门下的人
            return manager().findSelfUserPage(t, page);
        } else {// 查整个公司的人
            if (su.isAdmin()) {
                return manager().findSelfUserPage(t, page);
            } else {
                page.addRule(new Equal("appId", Long.parseLong(su.getAppId())));
                page.addRule(new Equal("userId", Long.valueOf(su.getId())));
                return manager().findPermedSelfUserPage(t, page);
            }
        }
    }

    @Override
    protected KhUser doSave(KhUser user, Trace t, AppVisitor av) {
        return super.doSave(user, t, av);
    }

    @RequestMapping(value = "/list4Role", method = RequestMethod.GET)
    public String list4Role(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap) {
        if (roleId != null && roleUuid != null) {
            KhRole role = khRoleManager.fetchByIdUuid(t, roleId, roleUuid);
            modelMap.put("roleName", role.getName());
        }

        ISessionUser su = Controllers.getSessionUser();
        List<KhRole> roles = khRoleManager.findCompanyAllRoles(t, Long.parseLong(su.getCompanyId()));
        modelMap.put("roles", roles);

        modelMap.put("roleId", roleId);
        modelMap.put("roleUuid", roleUuid);
        return "page/khself/KhUser/list4Role";
    }

    @RequestMapping(value = "/page4Role", method = RequestMethod.GET)
    public @ResponseBody Page<KhUser> page4Role(@RequestBody WebPage webPage, Trace t, AppVisitor av)
            throws IllegalParameterException {
        Page<KhUser> page = webPage.toPage();
        if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
            throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
        }

        ISessionUser su = Controllers.getSessionUser();
        Long companyId = Long.parseLong(su.getCompanyId());
        Equal r = (Equal) page.getRule4Field("companyId");
        if (r != null) {
            r.setValue(companyId);
        } else {
            r = new Equal("companyId", companyId);
            page.addRule(r);
        }

        Equal rr = (Equal) page.getRule4Field("roleId");
        if (rr != null) {
            KhRole role = getRoleManager().fetchById(t, (Long) rr.getValue());
            page.addRule(new Equal("type", role.getType()));
        }

        return manager().findPage4Role(t, page);
    }

    @RequestMapping(value = "/addRoles", method = RequestMethod.GET)
    public @ResponseBody Boolean addRoles(@RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userUuid", required = false) String userUuid,
            @RequestBody Map<String, Long> roleUuidIds, Trace t, ModelMap modelMap) {
        return khUserManager.addUserRoles(t, userId, userUuid, roleUuidIds);
    }

    @RequestMapping(value = "/removeRoles", method = RequestMethod.GET)
    public @ResponseBody Boolean removeRoles(@RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userUuid", required = false) String userUuid,
            @RequestBody Map<String, Long> roleUuidIds, Trace t, ModelMap modelMap) {
        return khUserManager.removeUserRoles(t, userId, userUuid, roleUuidIds);
    }

    @Override
    protected Page<KhUser> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
        Page<KhUser> page = webPage.toPage();
        Equal r = (Equal) page.getRule4Field("companyId");
        if (r != null) {
            r.setValue(Long.valueOf(av.getCompanyId()));
        } else {
            page.addRule(new Equal("companyId", Long.valueOf(av.getCompanyId())));
        }
        return manager().findPage4Select(t, page);
    }

}
