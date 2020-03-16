package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.BaseTreeManager;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.um.service.sys.IMenuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = IMenuManager.class, version = "${dubbo.service.version}")
@Module(name = "菜单")
public class MenuManager extends BaseTreeManager<Menu, IMenuService> implements IMenuManager {

    @Autowired
    private IMenuService menuService;

    @Override
    public List<Tree> getMenus(Trace t, Long appId) {
        return service().getMenus(t, appId);
    }

    @Override
    public List<Tree> getValidMenus(Trace t, Long appId) {
        return service().getValidMenus(t, appId);
    }

    @Override
    public List<Tree> getValidMenus(Trace t, String appCode) {
        return service().getValidMenus(t, appCode);
    }

    @Override
    public List<Menu> getValidMenuList(Trace t, String appCode) {
        return service().getValidMenuList(t, appCode);
    }

    @Override
    protected IMenuService service() {
        return menuService;
    }

    @Override
    public Tree getMenuTree(Trace t, String appCode) {
        return service().getMenuTree(t, appCode);
    }

}
