package com.linkallcloud.um.pc.controller.sys;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.web.perm.RequirePermissions;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IMenuManager;

@Controller
@RequestMapping(value = "/menu", method = RequestMethod.POST)
@Module(name = "菜单")
@RequirePermissions({ "sys_app-menu" })
public class MenuController {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IMenuManager menuManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public String list(Trace t, ModelMap modelMap) {
		Query query = new Query();
		query.addRule(new Equal("status", 0));
		List<Application> apps = applicationManager.find(t, query);
		modelMap.put("apps", apps);
		return "page/menu/menuTree";
	}

	@RequestMapping(value = "/loadTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadTree(Trace t, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, AppVisitor av) throws IllegalParameterException {
//		List<Tree> items = menuManager.getMenus(t, appId);
//		return new Result<>(items);
		return null;
	}

	@RequirePermissions({})
	@RequestMapping(value = "/loadValidTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadValidTree(Trace t, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, AppVisitor av) throws IllegalParameterException {
		List<Tree> items = menuManager.getValidMenus(t, appId);
		return new Result<>(items);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "appId") Long appId,
			@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		modelMap.put("appId", appId);
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return "page/menu/menuEdit";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Result<Menu> get(@RequestParam(value = "appId") Long appId,
			@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t) {
		Menu entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = menuManager.fetchByIdUuid(t, id, uuid);
		} else {
			entity = new Menu();
			entity.setParentId(parentId);
			entity.setAppId(appId);
			if (!entity.isTopParent()) {
				Menu parent = menuManager.fetchById(t, parentId);
				if (parent != null) {
					entity.setParentName(parent.getName());
				}
			}
		}
		if (entity.isTopParent()) {
			Application app = applicationManager.fetchById(t, entity.getAppId());
			if (app != null) {
				entity.setParentName(app.getName());
			}
		}
		return new Result<>(entity);
	}

	@WebLog(db = true)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Result<Menu> save(@RequestBody @Valid Menu entity, Trace t) {
		if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
			menuManager.update(t, entity);
		} else {
			Long id = menuManager.insert(t, entity);
			entity.setId(id);
		}
		entity = menuManager.fetchById(t, entity.getId());
		return new Result<>(entity);
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了菜单([(${id})]), TID:[(${tid})]")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t) {
		Boolean ret = menuManager.delete(t, id, uuid);
		return new Result<>(!ret.booleanValue(), "9010", "删除失败");
	}

	// @WebLog(db = true, desc = "用户([(${visitor.name})])[#
	// th:if=\"${status==0}\"]启用了[/][# th:if=\"${status!=0}\"]停用了[/]菜单([(${id})]),
	// TID:[(${tid})]")
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public @ResponseBody Result<Object> changeStatus(@RequestParam(value = "status") int status,
			@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t) {
		Boolean ret = menuManager.updateStatus(t, status, id, uuid);
		return new Result<>(!ret.booleanValue(), "9010", "删除失败");
	}

	@RequestMapping(value = "/getUmMenuTree", method = RequestMethod.GET)
	public @ResponseBody Result<Object> getUmTree(Trace t) throws IllegalParameterException {
		Tree root = null;//menuManager.getMenuTree(t, "lac_app_um");
		return new Result<>(root == null ? null : root.getChildren());
	}

}
