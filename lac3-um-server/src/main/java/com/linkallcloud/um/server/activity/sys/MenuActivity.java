package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseTreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.activity.sys.IMenuActivity;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuActivity extends BaseTreeActivity<Menu, IMenuDao> implements IMenuActivity {

    @Autowired
    private IMenuDao menuDao;

    @Autowired
    private IApplicationDao applicationDao;

    @Override
    public IMenuDao dao() {
        return menuDao;
    }

    @Override
    public List<Menu> findAppMenus(Trace t, Long appId, boolean valid) {
        return dao().findAppMenusWithButton(t, appId, valid);
    }

    @Override
    public List<Tree> getMenus(Trace t, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        return assembleMenuTrees(t, app, false);
    }

    private List<Tree> assembleMenuTrees(Trace t, Application app, boolean valid) {
        Tree root = app.toMenuRoot();
        root.setType("0");

        List<Menu> menus = dao().findAppMenusWithButton(t, app.getId(), valid);
        List<Tree> result = Trees.assembleDomain2List(root, menus);

        // List<Tree> result = Trees.assembleTreeList(root.getId(), menus);
        // result.add(root);
        Tree.sort(result);
        return result;
    }

    @Override
    public List<Tree> getValidMenus(Trace t, String appCode) {
        Application app = applicationDao.fetchByCode(t, appCode);
        if (app == null) {
            throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        return assembleMenuTrees(t, app, true);
    }

    @Override
    public List<Menu> getValidMenuList(Trace t, String appCode) {
        Application app = applicationDao.fetchByCode(t, appCode);
        if (app == null) {
            throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }
        return dao().findAppMenusWithButton(t, app.getId(), true);
    }

    @Override
    public List<Tree> getValidMenus(Trace t, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        return assembleMenuTrees(t, app, true);
    }

    @Override
    public List<Tree> getValidMenusWithButton(Trace t, Long appId) {
        Application app = applicationDao.fetchById(t, appId);
        if (app == null) {
            throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        Tree root = app.toMenuRoot();
        List<Menu> menus = dao().findAppMenusWithButton(t, appId, true);
        List<Tree> result = Trees.assembleDomain2List(root, menus);
        Tree.sort(result);
        return result;
    }

    @Override
    public Tree getMenuTree(Trace t, String appCode) {
        Application app = applicationDao.fetchByCode(t, appCode);
        if (app == null) {
            throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
        }

        Tree root = app.toMenuRoot();

        Query query = new Query();
        query.addRule(new Equal("appId", app.getId()));
        query.addRule(new Equal("status", 0));
        List<Menu> menus = dao().find(t, query);
        Trees.assembleDomain2Tree(root, menus);
        return root;
    }

    @Override
    protected void before(Trace t, boolean isNew, Menu entity) {
        if (entity.getParentId() == null || entity.getParentId() <= 0) {
            entity.setParentId(0L);
        }
        if (exist(t, entity, "govCode", entity.getGovCode())) {
            throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "菜单编号重复！");
        }
    }

    // @Override
    // public List<Tree> findKhCompanyValidMenus(Trace t, Long khCompanyId, Long
    // appId, String appUuid) {
    // Application app = applicationDao.fetchByIdUuid(t, appId, appUuid);
    // if (app == null) {
    // throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
    // }
    //
    // Tree root = generateApplicationMenuRoot(app);
    //
    // List<Tree> result = null;
    // List<Menu> menus = dao().findKhCompanyAppMenus(t, khCompanyId, appId, true);
    //
    // if (menus != null) {
    // result = Trees.assembleTreeList(root.getId(), menus);
    // }
    // if (result == null) {
    // result = new ArrayList<Tree>();
    // }
    // result.add(root);
    // return Trees.filterTreeNode(result);
    // }

    // @Override
    // public List<Tree> findYwCompanyValidMenus(Trace t, Long ywCompanyId, Long
    // appId, String appUuid) {
    // Application app = applicationDao.fetchByIdUuid(t, appId, appUuid);
    // if (app == null) {
    // throw new BaseRuntimeException("80000001", "无法查询对应的应用，可能是您的参数有误。");
    // }
    //
    // Tree root = generateApplicationMenuRoot(app);
    //
    // List<Tree> result = null;
    // List<Menu> menus = dao().findYwCompanyAppMenus(t, ywCompanyId, appId, true);
    //
    // if (menus != null) {
    // result = Trees.assembleTreeList(root.getId(), menus);
    // }
    // if (result == null) {
    // result = new ArrayList<Tree>();
    // }
    // result.add(root);
    // return Trees.filterTreeNode(result);
    // }
}
