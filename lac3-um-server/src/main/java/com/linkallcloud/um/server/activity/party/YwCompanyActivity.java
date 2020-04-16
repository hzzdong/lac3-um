package com.linkallcloud.um.server.activity.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.exception.AccountException;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IAreaDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;
import com.linkallcloud.um.server.dao.sys.IYwSystemConfigDao;

@Component
public class YwCompanyActivity extends CompanyActivity<YwCompany, IYwCompanyDao, YwUser, IYwUserDao, YwDepartment, IYwDepartmentDao> implements IYwCompanyActivity {

    @Autowired
    private IYwCompanyDao ywwCompanyDao;

    @Autowired
    private IYwUserDao ywUserDao;

    @Autowired
    private IYwDepartmentDao ywDepartmentDao;

//    @Autowired
//    private IYwRoleDao ywRoleDao;

    @Autowired
    private IAreaDao areaDao;

    @Autowired
    private IApplicationDao applicationDao;

    @Autowired
    private IMenuDao menuDao;

    @Autowired
    private IYwSystemConfigDao ywSystemConfigDao;

    public YwCompanyActivity() {
        super();
    }

    @Override
    protected IYwUserDao getUserDao() {
        return ywUserDao;
    }

    @Override
    public IYwCompanyDao dao() {
        return ywwCompanyDao;
    }

    @Override
    protected IYwDepartmentDao getDepartmentDao() {
        return ywDepartmentDao;
    }

    @Override
    public boolean checkUserExist(Trace t, boolean isNew, YwCompany entity) {
        if (isNew) {
            if (!Strings.isBlank(entity.getGovCode())) {
                YwUser dbUser = getUserDao().fecthByAccount(t, entity.getGovCode());
                if (dbUser != null) {
                    throw new AccountException(Exceptions.CODE_ERROR_PARAMETER,
                            "账号已经存在：" + entity.getGovCode());
                }

                Account account = accountDao.fecthByAccount(t, entity.getGovCode());
                if (account != null) {
                    throw new AccountException(Exceptions.CODE_ERROR_PARAMETER,
                            "账号已经存在：" + entity.getGovCode());
                }
            }
        }
        return false;
    }

    @Override
    protected void treeBefore(Trace t, boolean isNew, YwCompany entity) {
        Area area = areaDao.fetchById(t, entity.getAreaId());
        if (area != null) {
            entity.setLevel(area.getLevel());
        }

        super.treeBefore(t, isNew, entity);
    }

    @Override
    protected void autoCreateAdmin(Trace t, YwCompany entity) {
        YwUser user = new YwUser(entity.getName() + "_管理员", entity.getGovCode(), entity.getPhone(),
                entity.getGovCode().length() >= 6 ? entity.getGovCode()
                        : (entity.getGovCode() + Strings.dup('0', 6 - entity.getGovCode().length())));
        user.setSalt(user.generateUuid());
        user.setPassword(Securities.password4Src(user.getPassword(), user.getSalt()));
        user.setParent(entity);
        user.setCompanyId(entity.getId());
        user.setType(Domains.USER_ADMIN);
        getUserDao().insert(t, user);

        user.setCode(Domains.genDomainCode(entity, user));
        getUserDao().updateCode(t, user.getId(), user.getCode());

        autoCreateAccount(t, user);
        autoAddSysAdminRole(t, user);
    }

    @Override
    protected void autoAddSysAdminRole(Trace t, YwUser user) {
//        YwRole r = ywRoleDao.fetchByGovCode(t, YwRole.class.getSimpleName() + Domains.SYS_ADMIN_ROLE_STUFF);
//        if (r != null) {
//            ywRoleDao.addRoleUsers(t, r.getId(), new ArrayList<Long>(Arrays.asList(user.getId())));
//        }
    }

    @Override
    public Tree findCompanyValidMenuTree(Trace t, Long companyId, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new ArgException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        Tree root = app.toMenuRoot();
        List<Menu> menus = menuDao.findYwCompanyAppMenus(t, companyId, appId, true);
        // Trees.assembleTree(root, new CopyOnWriteArrayList<Menu>(menus));
        Trees.assembleDomain2Tree(root, menus);
        return root;
    }

    @Override
    public List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new ArgException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        YwCompany company = fetchById(t, companyId);
        if (company == null) {
            throw new ArgException("80000002", "无法查询对应的公司，可能是您的参数有误。");
        }

        List<Menu> menus = null;
        if (company.isTopParent()) {
            menus = menuDao.findAppMenusWithButton(t, app.getId(), true);
        } else {
            menus = menuDao.findYwCompanyAppMenusWithButton(t, companyId, appId, true);
        }

        Tree root = app.toMenuRoot();
        List<Tree> result = Trees.assembleDomain2List(root, menus);
        Tree.sort(result);
        return result;
    }

    @Override
    public PermedAreaVo findCompanyValidAreaResource(Trace t, Long companyId, Long appId) {
        YwCompany company = dao().fetchById(t, companyId);
        Application app = applicationDao.fetchById(t, appId);
        if (company.isTopParent()) {
            Long parentAreaId = getCompanyAreaRootIdBySystemConfig(t, companyId);
            List<Area> areas = areaDao.findByParent(t, parentAreaId, new Equal("status", 0));
            return assemblePermedAreaVo(t, parentAreaId, areas);
        } else if (!app.getCode().equals("lac_app_um") && !app.getCode().equals("lac_app_um_kh")) {
            Long parentAreaId = company.getAreaId();
            if (parentAreaId != null) {
                List<Area> areas = areaDao.findByParent(t, parentAreaId, new Equal("status", 0));
                return assemblePermedAreaVo(t, parentAreaId, areas);
            }
            return null;
        } else {
            List<Area> areas = areaDao.findPermedYwCompanyAppAreas(t, companyId, appId);
            if (areas != null && !areas.isEmpty()) {
                Long parentAreaId = areas.get(0).getParentId();
                return assemblePermedAreaVo(t, parentAreaId, areas);
            } else {
                throw new AuthException("100001", "请先给单位区域授权后再进行此操作");
            }
        }
    }

    @Override
    public Long getCompanyAreaRootIdBySystemConfig(Trace t, Long companyId) {
        YwSystemConfig sc = ywSystemConfigDao.fetchByCompanyId(t, companyId);
        if (sc != null) {
            if (sc.getRootAreaId() != null && sc.getRootAreaId().longValue() > 0) {
                return sc.getRootAreaId();
            } else {// 中华人民共和国
                return 0L;
            }
        } else {
            log.error("您所在公司的区域未设置，请联系管理员设置后再进场查询。companyId:" + companyId);
            throw new ArgException(Exceptions.CODE_ERROR_PARAMETER,
                    "您所在公司的区域未设置，请联系管理员设置后再进场查询。");
        }
    }

}
