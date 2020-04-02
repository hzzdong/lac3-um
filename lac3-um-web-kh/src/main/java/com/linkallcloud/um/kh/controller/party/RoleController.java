package com.linkallcloud.um.kh.controller.party;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.IRoleManager;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.web.utils.Controllers;

public abstract class RoleController<T extends Role, U extends User, M extends IRoleManager<T, U>, UM extends IUserManager<U>> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Mirror<T> mirror;

	@SuppressWarnings("unchecked")
	public RoleController() {
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

	protected abstract M manager();

	protected abstract UM userManager();

	/**
	 * 首页
	 * 
	 * @return
	 */
	protected String getMainPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/list";
	}

	/**
	 * 编辑页面
	 * 
	 * @return
	 */
	protected String getEditPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/edit";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return getMainPage();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody Result<Object> page(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
		ISessionUser su = Controllers.getSessionUser();
		int type = getRoleType();
		Page<T> page = webPage.toPage();
		page = manager().findCompanyRolePage(t, Long.parseLong(su.getCompanyId()), type, page);
		return new Result<Object>(page);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void view(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid,
			ModelMap modelMap) {
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		return edit(id, uuid, t, modelMap);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);

		if (id != null && id > 0 && uuid != null) {
			T entity = manager().fetchByIdUuid(t, id, uuid);
			modelMap.put("entity", entity);
		} else {
			T entity = mirror.born();
			ISessionUser su = Controllers.getSessionUser();
			entity.setParentId(Long.parseLong(su.getCompanyId()));
			modelMap.put("entity", entity);
		}

		return getEditPage();
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Result<Object> get(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t) {
		T entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = manager().fetchByIdUuid(t, id, uuid);
		} else {
			entity = mirror.born();
		}
		return new Result<Object>(entity);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> save(@RequestBody @Valid T entity, Trace t, AppVisitor av) {
		ISessionUser su = Controllers.getSessionUser();
		entity.setCompanyId(Long.valueOf(su.getCompanyId()));

		entity.setType(getRoleType());
		if (entity.getType() == Domains.ROLE_NORMAL) {
			entity.setParentId(Long.valueOf(su.getCompanyId()));
		} else {
			entity.setParentId(0L);
		}

		if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
			manager().update(t, entity);
		} else {
			Long id = manager().insert(t, entity);
			entity.setId(id);
		}
		return new Result<Object>(entity);
	}

	protected abstract int getRoleType();

	/**
	 * 删除
	 *
	 * @param id
	 * @param uuid
	 * @return
	 */
	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})]([(${id})]), TID:[(${tid})]")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		Boolean ret = manager().delete(t, id, uuid);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除失败");
	}

	/**
	 * 批量删除
	 *
	 * @param uuidIds
	 * @return
	 */
	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})]([(${uuidIds})]), TID:[(${tid})]")
	@RequestMapping(value = "deletes", method = RequestMethod.POST)
	public @ResponseBody Result<Object> deletes(@RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
		Boolean ret = manager().deletes(t, uuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除失败");
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public String select(@RequestBody List<StringRuleDescriptor> cnds,
			@RequestParam(value = "multi", required = false) boolean multi, ModelMap modelMap) {
		modelMap.put("cnds", cnds);
		modelMap.put("multi", multi);
		return getSelectPage();
	}

	protected String getSelectPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/select";
	}

	@RequestMapping(value = "/page4Select", method = RequestMethod.GET)
	public @ResponseBody Result<Object> page4Select(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
		Page<T> page = doPage4Select(webPage, t, av);
		return new Result<Object>(page);
	}

	protected Page<T> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
		Page<T> page = webPage.toPage();
		Equal r = (Equal) page.getRule4Field("userId");
		if (r != null) {
			U user = userManager().fetchById(t, (Long) r.getValue());
			page.addRule(new Equal("companyId", user.getCompanyId()));
		}
		return manager().findPage4Select(t, page);
	}

	// /**
	// * 获取用户拥有的角色分页列表
	// *
	// * @param query
	// * @param tid
	// * @param av
	// * @return
	// */
	// @RequestMapping(value = "/userRolePage", method = RequestMethod.GET)
	// public @ResponseBody Page userRolePage(@RequestBody WebPage webPage, Trace t,
	// AppVisitor av) {
	// return manager().findPage4User(t, webPage.toPage());
	// }
	//
	// /**
	// * 获取用户未拥有的角色分页列表
	// *
	// * @param query
	// * @param tid
	// * @param av
	// * @return
	// */
	// @RequestMapping(value = "/userNoRolePage", method = RequestMethod.GET)
	// public @ResponseBody Page userNoRolePage(@RequestBody WebPage webPage, Trace
	// t, AppVisitor av) {
	// return manager().findNoRolePage4User(t, webPage.toPage());
	// }

}
