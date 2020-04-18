package com.linkallcloud.um.kh.controller.party;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.um.iapi.party.IDepartmentManager;
import com.linkallcloud.um.iapi.party.IOrgManager;
import com.linkallcloud.um.iapi.party.IRoleManager;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.web.controller.BaseLController4ParentTree;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

public abstract class UserController<T extends User, S extends IUserManager<T>, R extends Role, RS extends IRoleManager<R, T>, P extends Org, PS extends IOrgManager<P>>
        extends BaseLController4ParentTree<T, S, P, PS> {
    // C extends Company, CS extends ICompanyManager<C>, D extends Department, DS extends IDepartmentManager<D>

    protected Log logger = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public UserController() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (logger.isWarnEnabled()) {
                logger.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    public Class<T> getDomainClass() {
        return (null == mirror) ? null : mirror.getType();
    }

    // protected abstract S getUserManager();

    protected abstract <CPS extends ICompanyManager<Company>> CPS getCompanyManager();

    protected abstract <DPS extends IDepartmentManager<Department>> DPS getDepartmentManager();

    protected abstract RS getRoleManager();

    @SuppressWarnings("unchecked")
    @Override
    protected PS parentManager(String parentClass) {
        if (!Strings.isBlank(parentClass) && parentClass.endsWith("Company")) {
            return (PS) getCompanyManager();
        } else {
            return (PS) getDepartmentManager();
        }
    }

    @Override
    protected String getMainPage() {
        return "page/party/" + getDomainClass().getSimpleName() + "/main";
    }

    @Override
    protected String getEditPage() {
        return "page/party/" + getDomainClass().getSimpleName() + "/edit";
    }

    @Override
    protected String getSelectPage() {
        return "page/party/" + getDomainClass().getSimpleName() + "/select";
    }

    @Override
    protected Page<T> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
        SessionUser su = Controllers.getSessionUser();
        Page<T> page = webPage.toPage();
        if (!page.hasRule4Field("companyId")) {
            page.addRule(new Equal("companyId", su.companyId()));
        }
        return manager().findPage(t, page);
    }

    @Override
    protected T doGet4Parent(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
        T entity = null;
        if (id != null && id > 0 && uuid != null) {
            entity = manager().fetchByIdUuid(t, id, uuid);
            parentId = entity.getParentId();
            parentClass = entity.getParentClass();
        } else {
            entity = mirror.born();
            entity.setParentId(parentId);
            entity.setParentClass(parentClass);
        }

        if (parentId != null && !Strings.isBlank(parentClass) && parentClass.endsWith("Company")) {
            Company company = (Company) parentManager(parentClass).fetchById(t, parentId);
            if (company != null) {
                entity.setOrgName(company.getName());
                entity.setParentId(-1 * entity.getParentId());
            }
        } else if (parentId != null && !Strings.isBlank(parentClass) && parentClass.endsWith("Department")) {
            Department depart = (Department) parentManager(parentClass).fetchById(t, parentId);
            if (depart != null) {
                entity.setOrgName(depart.getName());
            }
        }
        return entity;
    }

    @Override
    protected T doSave(T user, Trace t, AppVisitor av) {
        if (user.getParentId() != null && user.getParentId().longValue() < 0) {
            user.setParentId(-1 * user.getParentId());
        }

        if (user.getParentId() != null && !Strings.isBlank(user.getParentClass())
                && user.getParentClass().endsWith("Company")) {
            user.setCompanyId(user.getParentId());
        } else {
            SessionUser su = Controllers.getSessionUser();
            user.setCompanyId(su.companyId());
        }

        if (user.getId() != null && user.getId() > 0 && user.getUuid() != null) {
            manager().update(t, user);
        } else {
            Long id = manager().insert(t, user);
            user.setId(id);
        }
        return user;
    }

    // @RequestMapping(value = "/userRoles", method = RequestMethod.GET)
    // public String userRoles(@RequestParam(value = "userId") Long userId,
    // @RequestParam(value = "userUuid") String userUuid, ModelMap modelMap) {
    // modelMap.put("userId", userId);
    // modelMap.put("userUuid", userUuid);
    // return getUserRolesPage();
    // }
    //
    // protected String getUserRolesPage() {
    // return "page/party/" + getDomainClass().getSimpleName() + "/" + getDomainClass().getSimpleName() + "Roles";
    // }

    // @WebLog(db = true, desc =
    // "用户([(${visitor.name})])给用户([(${userId})])添加了角色([(${uuidIds})]),
    // TID:[(${tid})]")
    // @RequestMapping(value = "/addRoles", method = RequestMethod.POST)
    // public @ResponseBody Boolean addRoles(@RequestParam(value = "userId") Long
    // userId,
    // @RequestParam(value = "userUuid") String userUuid, @RequestBody Map<String,
    // Long> roleUuidIds,
    // Trace t, AppVisitor av) {
    // return getUserManager().addRoles2User(t, userId, userUuid, roleUuidIds);
    // }
    //
    // @WebLog(db = true, desc =
    // "用户([(${visitor.name})])给用户([(${userId})])删除了角色([(${uuidIds})]),
    // TID:[(${tid})]")
    // @RequestMapping(value = "/deleteRoles", method = RequestMethod.POST)
    // public @ResponseBody Boolean deleteRoles(@RequestParam(value = "userId") Long
    // userId,
    // @RequestParam(value = "userUuid") String userUuid, @RequestBody Map<String,
    // Long> roleUuidIds,
    // Trace t, AppVisitor av) {
    // return getUserManager().deleteRoles2User(t, userId, userUuid, roleUuidIds);
    // }

    @Override
    protected Page<T> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
        Page<T> page = webPage.toPage();
        Equal r = (Equal) page.getRule4Field("roleId");
        if (r != null) {
            R role = getRoleManager().fetchById(t, (Long) r.getValue());
            // if (role.getType() == Domains.ROLE_NORMAL) {
            // SessionUser su = Controllers.getSessionUser();
            // page.addRule(new Equal("companyId", su.companyId()));
            // }

            SessionUser su = Controllers.getSessionUser();
            page.addRule(new Equal("type", role.getType()));
            page.addRule(new Equal("companyId", su.companyId()));
        }
        return manager().findPage4Select(t, page);
    }

}
