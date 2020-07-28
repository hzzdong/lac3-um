package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.BaseTreeManager;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.um.service.sys.IMenuService;

@DubboService(interfaceClass = IMenuManager.class, version = "${dubbo.service.version}")
@Module(name = "菜单")
public class MenuManager extends BaseTreeManager<Menu, IMenuService> implements IMenuManager {

	@Autowired
	private IMenuService menuService;

	@Override
	public List<Tree> getMenus(Trace t, Sid appId, Boolean status) {
		return service().getMenus(t, appId, status);
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
	public Tree getMenuTree(Trace t, Sid appId, Boolean status) {
		return service().getMenuTree(t, appId, status);
	}

}
