package com.linkallcloud.um.server.manager.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IRoleManager;
import com.linkallcloud.um.service.party.IRoleService;
import com.linkallcloud.um.service.sys.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class RoleManager<T extends Role, U extends User, S extends IRoleService<T, U>>
        extends PartyManager<T, S> implements IRoleManager<T, U> {

    protected static Log logs = Logs.get();

    @Autowired
    protected IAreaService areaService;

    @Override
    public Page<T> findCompanyRolePage(Trace t, Long companyId, int type, Page<T> page)
            throws BaseRuntimeException {
        return service().findCompanyRolePage(t, companyId, type, page);
    }

    @Override
    public List<T> findCompanyRoles(Trace t, Long companyId, int type) throws BaseRuntimeException {
        return service().findCompanyRoles(t, companyId, type);
    }

    @Override
    public List<T> findCompanyAllRoles(Trace t, Long companyId) throws BaseRuntimeException {
        return service().findCompanyAllRoles(t, companyId);
    }

    @Override
    public List<T> findCompanyRolesByLevel(Trace t, Long companyId, Integer roleLevel) throws BaseRuntimeException {
        return service().findCompanyRolesByLevel(t, companyId, roleLevel);
    }

    @Override
    public Page<T> findCompanyAllRolePage(Trace t, Long companyId, Page<T> page)
            throws BaseRuntimeException {
        return service().findCompanyAllRolePage(t, companyId, page);
    }

    @Override
    public Page<T> findPage4User(Trace t, Page<T> page) {
        return service().findPage4User(t, page);
    }

    @Override
    public Page<T> findNoRolePage4User(Trace t, Page<T> page) {
        return service().findNoRolePage4User(t, page);
    }

    @Override
    public List<T> find4User(Trace t, Long userId) {
        return service().find4User(t, userId);
    }

    @Override
    public boolean addRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds) {
        return service().addRoleUsers(t, roleId, roleUuid, userUuidIds);
    }

    @Override
    public boolean removeRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds) {
        return service().removeRoleUsers(t, roleId, roleUuid, userUuidIds);
    }

    @Override
    public boolean clearRoleUsers(Trace t, Long roleId, String roleUuid) {
        return service().clearRoleUsers(t, roleId, roleUuid);
    }

    @Override
    public List<U> getRoleUsers(Trace t, Long roleId, String roleUuid) {
        return service().getRoleUsers(t, roleId, roleUuid);
    }

    @Override
    public boolean addRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds) {
        return service().addRoleApps(t, roleId, roleUuid, appUuidIds);
    }

    @Override
    public boolean removeRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds) {
        return service().removeRoleApps(t, roleId, roleUuid, appUuidIds);
    }

    @Override
    public Boolean saveRoleAppMenuPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
                                       Map<String, Long> menuUuidIds) {
        return service().saveRoleAppMenuPerm(t, roleId, roleUuid, appId, appUuid, menuUuidIds);
    }

    @Override
    public Boolean saveRoleAppOrgPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
                                      Map<String, Long> orgUuidIds) {
        return service().saveRoleAppOrgPerm(t, roleId, roleUuid, appId, appUuid, orgUuidIds);
    }

    @Override
    public Boolean saveRoleAppAreaPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
                                       Map<String, Long> areaUuidIds) {
        return service().saveRoleAppAreaPerm(t, roleId, roleUuid, appId, appUuid, areaUuidIds);
    }

    @Override
    public List<Tree> findPermedMenus(Trace t, Long companyId, Long roleId, Long appId) {
        List<Tree> items = findCompanyValidMenus(t, companyId, appId);
        Long[] permedMenuIds = service().findPermedMenuIds(t, roleId, appId);
        if (permedMenuIds != null && permedMenuIds.length > 0) {
            CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedMenuIds);
            checkMenus(t, items, pmids);
        }
        return items;
    }

    protected abstract List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId);

    private void checkMenus(Trace t, List<Tree> items, CopyOnWriteArrayList<Long> permedItemIds) {
        if (items == null || items.isEmpty() || permedItemIds == null || permedItemIds.isEmpty()) {
            return;
        }
        for (Tree item : items) {
            for (Long pid : permedItemIds) {
                if (pid.toString().equals(item.getId())) {
                    item.setChecked(true);
                    permedItemIds.remove(pid);
                    break;
                }
            }
        }
    }

    @Override
    public List<Tree> findPermedOrgs(Trace t, Long companyId, Long roleId, Long appId) {
        List<Tree> items = findCompanyValidOrgs(t, companyId);
        Long[] permedItemIds = service().findPermedOrgIds(t, roleId, appId);
        if (permedItemIds != null && permedItemIds.length > 0) {
            CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedItemIds);
            checkMenus(t, items, pmids);
        }
        return items;
    }

    protected abstract List<Tree> findCompanyValidOrgs(Trace t, Long companyId);

    @Override
    public PermedAreaVo findPermedRoleAppAreas(Trace t, Long parentAreaId, Long companyId, Long roleId, Long appId) {
        Long[] permedItemIds = service().findPermedAreaIds(t, roleId, appId);

        PermedAreaVo result = null;
        if (parentAreaId != null && parentAreaId.longValue() > 0) {
            Long companyRootAreaId = getCompanyAreaRootId4Role(t, companyId);// getRoleAreaRootId(t, roleId, appId);
            if (parentAreaId.equals(companyRootAreaId)) {
                result = findCompanyValidAreaResource4Role(t, companyId);// findRoleValidAreaResource(t, roleId, appId);
            } else {
                result = areaService.findValidAreaResourceByParent(t, parentAreaId);
            }
        } else {
            if (permedItemIds != null && permedItemIds.length > 0) {
                Area area = areaService.fetchById(t, permedItemIds[0]);
                parentAreaId = area.getParentId();
                Long companyRootAreaId = getCompanyAreaRootId4Role(t, companyId);//getRoleAreaRootId(t, roleId, appId);
                if (parentAreaId.equals(companyRootAreaId)) {
                    result = findCompanyValidAreaResource4Role(t, companyId);// findRoleValidAreaResource(t, roleId, appId);
                } else {
                    result = areaService.findValidAreaResourceByParent(t, parentAreaId);
                }
            } else {
                result = findCompanyValidAreaResource4Role(t, companyId);// findRoleValidAreaResource(t, roleId, appId);
            }
        }

        if (result.getAreaNodes() != null && result.getAreaNodes().size() > 0 && permedItemIds != null
                && permedItemIds.length > 0) {
            CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedItemIds);
            checkMenus(t, result.getAreaNodes(), pmids);
        }
        return result;
    }

    protected PermedAreaVo assemblePermedAreaVo(Trace t, Long parentAreaId, List<Area> areas) {
        PermedAreaVo result = new PermedAreaVo();
        result.setParentAreaId(parentAreaId);
        if (0L != result.getParentAreaId()) {
            Area parent = areaService.fetchById(t, result.getParentAreaId());
            if (parent != null) {
                result.setParentAreaName(parent.getName());
            }
        } else {
            result.setParentAreaName("中华人民共和国");
        }

        if (areas != null && !areas.isEmpty()) {
            List<Tree> anodes = new ArrayList<>();
            for (int i = 0; i < areas.size(); i++) {
                Tree tn = areas.get(i).toTreeNode();
                tn.setpId(null);
                anodes.add(tn);
            }
            result.setAreaNodes(Trees.filterTreeNode(anodes));
        }
        return result;
    }

    @Override
    public List<T> findCompanyRoles4Me(Trace t, long userId) {
        return service().findCompanyRoles4Me(t, userId);
    }
}
