package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.service.BaseTreeService;
import com.linkallcloud.um.activity.sys.IMenuActivity;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.service.sys.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService extends BaseTreeService<Menu, IMenuActivity> implements IMenuService {

	@Autowired
	private IMenuActivity menuActivity;

	@Override
	public IMenuActivity activity() {
		return menuActivity;
	}

	@Override
	public List<Menu> findAppMenus(Trace t, Long appId, boolean valid) {
		return activity().findAppMenus(t, appId, valid);
	}

	@Override
	public List<Tree> getMenus(Trace t, Sid appId, Boolean status) {
		return activity().getMenus(t, appId, status);
	}

	@Override
	public List<Tree> getValidMenus(Trace t, String appCode) {
		return activity().getValidMenus(t, appCode);
	}

	@Override
	public List<Menu> getValidMenuList(Trace t, String appCode) {
		return activity().getValidMenuList(t, appCode);
	}

	@Override
	public List<Tree> getValidMenus(Trace t, Long appId) {
		return activity().getValidMenus(t, appId);
	}

	@Override
	public List<Tree> getValidMenusWithButton(Trace t, Long appId) {
		return activity().getValidMenusWithButton(t, appId);
	}

	@Override
	public Tree getMenuTree(Trace t, Sid appId, Boolean status) {
		return activity().getMenuTree(t, appId, status);
	}

}
