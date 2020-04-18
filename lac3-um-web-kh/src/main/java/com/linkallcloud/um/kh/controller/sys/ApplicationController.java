package com.linkallcloud.um.kh.controller.sys;

import java.util.List;

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
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
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

    @RequestMapping(value = "/list4SubKhCompany", method = RequestMethod.GET)
    public String list4SubKhCompany(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
        if (id != null && uuid != null) {
            KhCompany company = khCompanyManager.fetchByIdUuid(t, id, uuid);
            modelMap.put("khCompanyName", company.getName());
        }

        SessionUser su = Controllers.getSessionUser();
        List<KhCompany> companies =
                khCompanyManager.findDirectCompaniesByParentId(t, su.companyId());
        modelMap.put("companies", companies);

        modelMap.put("khCompanyId", id == null ? "" : id);
        modelMap.put("khCompanyUuid", uuid);
        return "page/Application/list4SubKhCompany";
    }

    @RequestMapping(value = "/page4SubKhCompany", method = RequestMethod.GET)
    public @ResponseBody Page<Application> page4SubKhCompany(@RequestBody WebPage webPage, Trace t, AppVisitor av)
            throws IllegalParameterException {
        Page<Application> page = webPage.toPage();
        if (!page.hasRule4Field("khCompanyId") || !page.hasRule4Field("khCompanyUuid")) {
            throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER,
                    "khCompanyId,khCompanyUuid参数错误。");
        }
        SessionUser su = Controllers.getSessionUser();
        page.addRule(new Equal("parentCompanyId", su.companyId()));
        return manager().findPage4KhCompany(t, page);
    }

    @RequestMapping(value = "/list4SelfKhRole", method = RequestMethod.GET)
    public String list4SelfKhRole(@RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleUuid", required = false) String roleUuid, Trace t, ModelMap modelMap) {
        if (roleId != null && !Strings.isBlank(roleUuid)) {
            KhRole role = khRoleManager.fetchByIdUuid(t, roleId, roleUuid);
            modelMap.put("roleName", role.getName());
        }

        SessionUser su = Controllers.getSessionUser();
        modelMap.put("khCompanyId", su.companyId());

        List<KhRole> roles = khRoleManager.findCompanyRoles(t, su.companyId(), Domains.ROLE_NORMAL);
        modelMap.put("roles", roles);

        modelMap.put("roleId", roleId);
        modelMap.put("roleUuid", roleUuid);
        return "page/Application/list4SelfKhRole";
    }

    @RequestMapping(value = "/page4SelfKhRole", method = RequestMethod.GET)
    public @ResponseBody Page<Application> page4SelfKhRole(@RequestBody WebPage webPage, Trace t, AppVisitor av)
            throws IllegalParameterException {
        Page<Application> page = webPage.toPage();
        if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
            throw new IllegalParameterException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
        }
        return manager().findPage4KhRole(t, page);
    }

    @Override
    protected Page<Application> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
        Page<Application> page = webPage.toPage();
        addAreaCnd2Page(page, av);
        if (page.hasRule4Field("command")) {
            Equal r = (Equal) page.getRule4Field("command");
            if (r.getValue().toString().equals("subKhCompanyApp4Perm")) {
                KhCompany company = khCompanyManager.fetchById(t, av.companyId());
                Long rootKhCompanyId = Domains.parseMyRootCompanyId(company.getCode());
                page.addRule(new Equal("rootKhCompanyId", rootKhCompanyId));
            }
        } else {
            if (av.getType().equals(KhUser.class.getSimpleName())) {
                if (!page.hasRule4Field("roleType")) {
                    page.addRule(new Equal("roleType", "KhRole"));
                }
                page.addRule(new Equal("khCompanyId", av.companyId()));
            }
        }

        return manager().findPage4Select(t, page);
    }

}
