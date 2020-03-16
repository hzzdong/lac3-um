package com.linkallcloud.um.server.manager.party;

import java.util.List;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.*;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.service.party.*;
import com.linkallcloud.um.service.sys.IAreaService;
import com.linkallcloud.web.session.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.Service;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.party.IKhUserManager;

@Service(interfaceClass = IKhUserManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "用户")
public class KhUserManager
        extends UserManager<KhUser, IKhUserService, KhRole, IKhRoleService, KhCompany, IKhCompanyService>
        implements IKhUserManager {

    @Autowired
    private IKhUserService khUserService;

    @Autowired
    private IKhRoleService khRoleService;

    @Autowired
    private IKhCompanyService khCompanyService;

    @Autowired
    private IKhDepartmentService khDepartmentService;

    @Autowired
    private IAreaService areaService;

    @Override
    protected IKhUserService service() {
        return khUserService;
    }

    @Override
    protected IKhRoleService roleService() {
        return khRoleService;
    }

    @Override
    protected IKhCompanyService companyService() {
        return khCompanyService;
    }

    @Override
    public Page<KhUser> findSelfUserPage(Trace t, Page<KhUser> page) {
        return service().findSelfUserPage(t, page);
    }

    @Override
    public Page<KhUser> findPermedSelfUserPage(Trace t, Page<KhUser> page) {
        return service().findPermedSelfUserPage(t, page);
    }

    @Override
    protected List<Application> findApplicaitons4User(Trace t, Long userId) {
        return applicationService.find4KhUser(t, userId);
    }


    @Override
    public SessionUser assembleSessionUser(Trace t, String loginName, String appCode) {
        if (Strings.isBlank(loginName) || Strings.isBlank(appCode)) {
            throw new ArgException("Arg", "loginName和AppCode都不能为空");
        }

        KhUser dbUser = this.fecthByAccount(t, loginName);
        if (dbUser != null) {
            Company dbCompany = companyService().fetchById(t, dbUser.getCompanyId());
            String orgName = dbCompany.getName();
            if (KhDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
                KhDepartment parent = khDepartmentService.fetchById(t, dbUser.getParentId());
                if (parent != null) {
                    orgName = parent.getFullName();
                }
            }
            SessionUser su = new SessionUser(String.valueOf(dbUser.getId()), dbUser.getUuid(), dbUser.getAccount(),
                    dbUser.getName(), dbUser.getUserType(), dbUser.getCompanyId().toString(), dbCompany.getName(),
                    dbUser.getParentId() != null ? dbUser.getParentId().toString() : dbUser.getCompanyId().toString(),
                    orgName, dbUser.getClass().getSimpleName().substring(0, 2));
            su.setAdmin(dbUser.isAdmin());

            if (dbCompany.getAreaId() != null) {
                Area area = areaService.fetchById(t, dbCompany.getAreaId());
                if (area != null) {
                    su.setAreaInfo(area.getId(), area.getLevel(), area.getName());
                }
            }

            Application app = applicationService.fetchByCode(t, appCode);
            String[] perms = this.getUserAppPermissions4Menu(t, Long.valueOf(su.getId()), app.getId());
            su.setPermissions(perms, null, null);
            su.setAppInfo(app.getId().toString(), app.getUuid(), app.getName());
            return su;
        }
        throw new ArgException("Arg", "Account或AppCode参数错误");
    }

}
