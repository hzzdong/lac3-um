package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.server.dao.party.*;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IAreaDao;
import com.linkallcloud.um.server.dao.sys.IKhSystemConfigDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;
import com.linkallcloud.um.server.utils.UmTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KhCompanyActivity extends CompanyActivity<KhCompany, IKhCompanyDao, KhUser, IKhUserDao, KhDepartment, IKhDepartmentDao> implements IKhCompanyActivity {

    @Autowired
    private IKhCompanyDao khCompanyDao;

    @Autowired
    private IKhUserDao khUserDao;

    @Autowired
    private IYwCompanyDao ywCompanyDao;

    @Autowired
    private IYwUserDao ywUserDao;

    @Autowired
    private IKhDepartmentDao khDepartmentDao;

//    @Autowired
//    private IKhRoleDao khRoleDao;

    @Autowired
    private IAreaDao areaDao;

    @Autowired
    private IKhSystemConfigDao khSystemConfigDao;

    @Autowired
    private IApplicationDao applicationDao;

    @Autowired
    private IMenuDao menuDao;

    @Override
    protected IKhUserDao getUserDao() {
        return khUserDao;
    }

    @Override
    public IKhCompanyDao dao() {
        return khCompanyDao;
    }

    @Override
    protected IKhDepartmentDao getDepartmentDao() {
        return khDepartmentDao;
    }

    @Override
    protected void updateAreaFieldsIfModified(Trace t, KhCompany entity) {
        if (entity == null) {
            return;
        }
        KhCompany dbEntity = dao().fetchById(t, entity.getId());
        if (dbEntity == null) {
            return;
        }

        if (!dbEntity.getAreaId().equals(entity.getAreaId())) {
            updateAreaFields(t, entity);
        }
    }

    @Override
    protected void updateAreaFields(Trace t, KhCompany entity) {
        if (entity == null || entity.getAreaId() == null || entity.getAreaId().longValue() <= 0) {
            return;
        }
        Area area = areaDao.fetchById(t, entity.getAreaId());
        if (area != null) {
            entity.setLevel(area.getLevel());
            String[] aids = area.getCode().split(area.codeTag());
            if (aids != null && aids.length > 0) {
                for (int i = 0; i < aids.length; i++) {
                    String aid = aids[i];
                    if (!Strings.isBlank(aid)) {
                        entity.setAreaLevelId(i + 1, Long.parseLong(aid));
                    }
                }
            }
            dao().updateAreaFields(t, entity);
        }
    }

    @Override
    protected void autoAddSysAdminRole(Trace t, KhUser user) {
//        KhRole r = khRoleDao.fetchByGovCode(t, KhRole.class.getSimpleName() + Domains.SYS_ADMIN_ROLE_STUFF);
//        if (r != null) {
//            khRoleDao.addRoleUsers(t, r.getId(), new ArrayList<Long>(Arrays.asList(user.getId())));
//        }
    }

    @Override
    public List<KhCompany> find(Trace t, Query query) {
        try {
            UmTools.addAreaCnds4YwUserAppPermission(t, query, ywCompanyDao, ywUserDao, areaDao);
        } catch (BaseException e) {
            return null;
        }
        return super.find(t, query);
    }

    /**
     * 根据user的区域权限，查找客户单位
     *
     * @param t
     * @param page 必须包含ywUserId，appId参数条件
     * @return
     */
    @Override
    public Page<KhCompany> findPage(Trace t, Page<KhCompany> page) {
        try {
            UmTools.addAreaCnds4YwUserAppPermission(t, page, ywCompanyDao, ywUserDao, areaDao);
        } catch (BaseException e) {
            return page;
        }
        return super.findPage(t, page);
    }

    /**
     * 根据user的区域权限，查找客户单位
     *
     * @param t
     * @param page 必须包含ywUserId，appId参数条件
     * @return
     */
    @Override
    public Page<KhCompany> findPage4Select(Trace t, Page<KhCompany> page) {
        try {
            UmTools.addAreaCnds4YwUserAppPermission(t, page, ywCompanyDao, ywUserDao, areaDao);
        } catch (BaseException e) {
            return page;
        }
        return super.findPage4Select(t, page);
    }

    public List<KhCompany> countByArea4id(Trace t, KhCompany entity) {
        return khCompanyDao.countByArea4id(t, entity);
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
        KhCompany khCompany = fetchByIdUuid(t, id, uuid);
        if (khCompany != null) {
            List<Application> checkedEntities = findAppsByUuidIds(t, appUuidIds);
            if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == appUuidIds.size()) {
                List<Long> appIds = Domains.parseIds(appUuidIds);
                return dao().addApps(t, id, appIds);
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
        KhCompany khCompany = fetchByIdUuid(t, id, uuid);
        if (khCompany != null) {
            List<Application> checkedEntities = findAppsByUuidIds(t, appUuidIds);
            if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == appUuidIds.size()) {
                List<Long> appIds = Domains.parseIds(appUuidIds);
                return dao().removeApps(t, id, appIds);
            }
        }
        return false;
    }

    public List<Application> findAppsByUuidIds(Trace t, Map<String, Long> appUuidIds) {
        List<Long> ids = Domains.parseIds(appUuidIds);
        if (ids != null && ids.size() > 0) {
            List<Application> entities = applicationDao.findByIds(t, ids);
            if (entities != null && !entities.isEmpty()) {
                List<Application> results = new ArrayList<Application>();
                for (Application entity : entities) {
                    if (entity.getUuid() != null && entity.getId().equals(appUuidIds.get(entity.getUuid()))) {
                        results.add(entity);
                    }
                }
                return results;
            }
        }
        return null;
    }

    // @Override
    // public Long getCompanyAreaRootId(Trace t, Long companyId, Long appId) {
    // Long[] permedItemIds = findPermedCompanyAppAreas(t, companyId, appId);
    // if (permedItemIds != null && permedItemIds.length > 0) {
    // Area area = areaDao.fetchById(t, permedItemIds[0]);
    // if (area != null) {
    // return area.getParentId();
    // }
    // }
    //
    // KhCompany company = dao().fetchById(t, companyId);
    // if (company.isTopParent()) {
    // return getCompanyAreaRootIdBySystemConfig(t, companyId);
    // } else {
    // KhCompany parent = dao().fetchById(t, company.getParentId());
    // if (parent.isTopParent()) {
    // return getCompanyAreaRootIdBySystemConfig(t, company.getParentId());
    // } else {
    // permedItemIds = findPermedCompanyAppAreas(t, parent.getId(), appId);
    // if (permedItemIds != null && permedItemIds.length > 0) {
    // Area area = areaDao.fetchById(t, permedItemIds[0]);
    // if (area != null) {
    // return area.getParentId();
    // }
    // }
    // }
    // }
    // return 0L;
    // }

    @Override
    public Long getCompanyAreaRootIdBySystemConfig(Trace t, Long companyId) {
        KhSystemConfig sc = khSystemConfigDao.fetchByCompanyId(t, companyId);
        if (sc != null && sc.getRootAreaId() != null) {
            if (sc.getRootAreaId().longValue() > 0) {
                return sc.getRootAreaId();
            } else {// 中华人民共和国
                return 0L;
            }
        } else {
            log.error("您所在公司的区域为设置，请联系管理员设置后再进场查询。companyId:" + companyId);
            throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "您所在公司的区域未设置，请联系管理员设置后再进场查询。");
        }
    }

    @Override
    public Tree findCompanyValidMenuTree(Trace t, Long companyId, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new ArgException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        Tree root = app.toMenuRoot();
        List<Menu> menus = menuDao.findKhCompanyAppMenusWithButton(t, companyId, appId, true);
        Trees.assembleDomain2Tree(root, menus);
        return root;
    }

    @Override
    public List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new ArgException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        Tree root = app.toMenuRoot();
        List<Menu> menus = menuDao.findKhCompanyAppMenusWithButton(t, companyId, appId, true);
        List<Tree> result = Trees.assembleDomain2List(root, menus);
        Tree.sort(result);
        return result;
    }

    @Override
    public PermedAreaVo findCompanyValidAreaResource(Trace t, Long companyId, Long appId) {
        KhCompany company = dao().fetchById(t, companyId);
        if (company.isTopParent()) {
            Long parentAreaId = getCompanyAreaRootIdBySystemConfig(t, companyId);
            List<Area> areas = areaDao.findByParent(t, parentAreaId, new Equal("status", 0));
            return assemblePermedAreaVo(t, parentAreaId, areas);
        } else {
            List<Area> areas = areaDao.findPermedKhCompanyAppAreas(t, companyId, appId);
            if (areas != null && !areas.isEmpty()) {
                Long parentAreaId = areas.get(0).getParentId();
                return assemblePermedAreaVo(t, parentAreaId, areas);
            } else {
                throw new ArgException("100001", "请先给单位区域授权后再进行此操作");
            }
        }
    }

}
