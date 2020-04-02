package com.linkallcloud.um.kh.controller.party;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
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
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.web.utils.Controllers;

public abstract class CompanyTreeController<C extends Company, CS extends ICompanyManager<C>, U extends User, US extends IUserManager<U>> {

	protected Log log = Logs.get();

	protected Mirror<C> cmirror;
	protected Mirror<U> umirror;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@SuppressWarnings("unchecked")
	public CompanyTreeController() {
		super();
		try {
			cmirror = Mirror.me((Class<C>) Mirror.getTypeParams(getClass())[0]);
			umirror = Mirror.me((Class<U>) Mirror.getTypeParams(getClass())[2]);
		} catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("!!!Fail to get TypeParams for self!", e);
			}
		}
	}

	public Class<C> getCompanyClass() {
		return (null == cmirror) ? null : cmirror.getType();
	}

	public Class<U> getUserClass() {
		return (null == umirror) ? null : umirror.getType();
	}

	protected abstract CS getComapnyManager();

	protected abstract US getUserManager();

	/**
	 * 首页
	 * 
	 * @return
	 */
	protected String getTreeMainPage() {
		return "page/party/" + getCompanyClass().getSimpleName() + "/tree";
	}

	/**
	 * 编辑页面
	 * 
	 * @return
	 */
	protected String getTreeEditPage() {
		return "page/party/" + getCompanyClass().getSimpleName() + "/edit";
	}

	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public String toTree() {
		return getTreeMainPage();
	}

	@RequestMapping(value = "/loadTree", method = RequestMethod.GET)
	public @ResponseBody Result<Object> loadTree(Trace t, AppVisitor av) {
		ISessionUser su = Controllers.getSessionUser();
		Application app = applicationManager.fetchByCode(t, "lac_app_um_kh");
		List<Tree> nodeList = getComapnyManager().getPermedCompanyOrgs(t, app.getId(), Long.parseLong(su.getId()));
		return new Result<>(nodeList);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass, Trace t, ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		modelMap.put("parentClass", parentClass);
		modelMap.put("id", null);
		modelMap.put("uuid", null);
		return getTreeEditPage();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		modelMap.put("parentClass", parentClass);
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return getTreeEditPage();
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Result<Object> get(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, AppVisitor av) {
		C entity = doGet(parentId, parentClass, id, uuid, t, av);
		return new Result<>(entity);
	}

	protected C doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		C entity = null;
		if (id != null && uuid != null) {
			entity = getComapnyManager().fetchByIdUuid(t, id, uuid);
		} else {
			entity = cmirror.born();

			ISessionUser su = Controllers.getSessionUser();
			entity.setParentId(Long.valueOf(su.getCompanyId()));
			entity.setParentClass(getCompanyClass().getSimpleName());
			C company = getComapnyManager().fetchById(t, Long.valueOf(su.getCompanyId()));
			if (company != null) {
				entity.setOrgName(company.getName());
			}
		}
		return entity;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> save(@RequestBody C entity, Trace t, AppVisitor av) {
		ISessionUser su = Controllers.getSessionUser();
		entity.setParentId(Long.valueOf(su.getCompanyId()));
		entity.setParentClass(getCompanyClass().getSimpleName());
		if (entity.getId() != null && entity.getUuid() != null) {
			getComapnyManager().update(t, entity);
		} else {
			Long id = getComapnyManager().insert(t, entity);
			entity.setId(id);
		}
		Tree tree = entity.toTreeNode();
		return new Result<Object>(tree);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})]([(${id})]), TID:[(${tid})]")
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		Boolean ret = getComapnyManager().delete(t, id, uuid);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除失败！");
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])修改 [(${domainShowName})]([(${id})])的状态为([(${status})]), TID:[(${tid})]")
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public @ResponseBody Result<Object> changeStatus(@RequestParam(value = "status") int status,
			@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		Boolean ret = getComapnyManager().updateStatus(t, status, id, uuid);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

	/**************************** 机构领导设置 ****************************/

	@RequestMapping(value = "/leaders", method = RequestMethod.GET)
	public String leaders(@RequestParam(value = "orgId") Long orgId, @RequestParam(value = "orgUuid") String orgUuid,
			ModelMap modelMap) {
		modelMap.put("orgId", orgId);
		modelMap.put("orgUuid", orgUuid);
		return getOrgLeaderPage();
	}

	private String getOrgLeaderPage() {
		return "page/party/" + getCompanyClass().getSimpleName() + "/leaders";
	}

	@RequestMapping(value = "/leaderPage", method = RequestMethod.GET)
	public @ResponseBody Page<U> page(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
		// return getUserService().findLeaderPage(t, webPage.toPage());
		return null;
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])添加 [(${domainShowName})]领导班子成员([(${userUuidIds})]), TID:[(${tid})]")
	@RequestMapping(value = "/addLeaders", method = RequestMethod.POST)
	public @ResponseBody Result<Object> addLeaders(@RequestParam(value = "orgId") Long orgId,
			@RequestParam(value = "orgUuid") String orgUuid, @RequestBody Map<String, Long> userUuidIds, Trace t) {
		Boolean ret = getComapnyManager().addLeaders(t, orgId, orgUuid, userUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])删除 [(${domainShowName})]的领导班子成员([(${userUuidIds})]), TID:[(${tid})]")
	@RequestMapping(value = "/deleteLeaders", method = RequestMethod.POST)
	public @ResponseBody Result<Object> deleteLeaders(@RequestParam(value = "orgId") Long orgId,
			@RequestParam(value = "orgUuid") String orgUuid, @RequestBody Map<String, Long> userUuidIds, Trace t) {
		Boolean ret = getComapnyManager().deleteLeaders(t, orgId, orgUuid, userUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

}
