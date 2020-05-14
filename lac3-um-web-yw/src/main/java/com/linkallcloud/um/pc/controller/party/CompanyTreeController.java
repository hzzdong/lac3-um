package com.linkallcloud.um.pc.controller.party;

import java.util.ArrayList;
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
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.um.iapi.party.IDepartmentManager;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;

public abstract class CompanyTreeController<C extends Company, CS extends ICompanyManager<C, U>, U extends User, US extends IUserManager<U>, D extends Department, DS extends IDepartmentManager<D, U>> {

	protected Log log = Logs.get();

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	protected Mirror<C> cmirror;
	protected Mirror<U> umirror;

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

	protected abstract DS getDepartmentManager();

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
	public String tree(Trace t, ModelMap modelMap, AppVisitor av) {
		return toTree(t, modelMap, av);
	}

	protected String toTree(Trace t, ModelMap modelMap, AppVisitor av) {
		return getTreeMainPage();
	}

	@RequestMapping(value = "/loadTree", method = RequestMethod.GET)
	public @ResponseBody Result<Object> loadTree(Trace t, AppVisitor av) {
		Tree root = getComapnyManager().getPermedCompanyTree(t, av.appId(), av.id());
		return new Result<>(root.getChildren());
	}

	@RequestMapping(value = "/treeView", method = RequestMethod.GET)
	public String viewTree(@RequestParam(value = "companyId", required = false) Long companyId, Trace t,
			ModelMap modelMap, AppVisitor av) {
		modelMap.put("companyId", companyId);
		return "page/party/YwCompany/treeView";
	}

	@RequestMapping(value = "/loadFullTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadFullTree(
			@RequestParam(value = "companyId", required = false) Long companyId, Trace t, AppVisitor av) {
//		Long myCompanyId = av.companyId();
//		if (companyId != null && companyId < myCompanyId) {
//			myCompanyId = companyId;
//		}
//		List<Tree> nodes = getComapnyManager().getCompanyFullOrgTreeList(t, myCompanyId);
//
//		if (nodes == null || nodes.isEmpty()) {
//			nodes = new ArrayList<Tree>();
//			nodes.add(new Tree("0", "", "无数据"));
//		}
//		return new Result<List<Tree>>(nodes);
		return null;
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
	public @ResponseBody Result<C> get(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, AppVisitor av) {
		C entity = null;
		if (id != null && uuid != null) {
			entity = getComapnyManager().fetchByIdUuid(t, id, uuid);
		} else {
			entity = cmirror.born();

			entity.setParentId(av.companyId());
			entity.setParentClass(getCompanyClass().getSimpleName());
			C company = getComapnyManager().fetchById(t, av.companyId());
			if (company != null) {
				entity.setOrgName(company.getName());
			}
		}
		return new Result<C>(entity);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Tree> save(@RequestBody C entity, Trace t, AppVisitor av) {
		entity.setParentId(av.companyId());
		entity.setParentClass(getCompanyClass().getSimpleName());
		if (entity.getId() != null && entity.getUuid() != null) {
			getComapnyManager().update(t, entity);
		} else {
			Long id = getComapnyManager().insert(t, entity);
			entity.setId(id);
		}

		Tree companyTreeNode = entity.toTreeNode();
		companyTreeNode.setId("-" + companyTreeNode.getId());
		companyTreeNode.setpId("-" + companyTreeNode.getpId());
		return new Result<Tree>(companyTreeNode);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})]([(${id})]), TID:[(${tid})]")
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		List<C> subCompanies = getComapnyManager().findSubCompanies(t, id);
		if (subCompanies != null && !subCompanies.isEmpty()) {
			return new Result<Object>(Exceptions.CODE_ERROR_DELETE, "删除单位失败，因为单位下存在子单位，请先删除子单位后进行删除。");
		}

		List<D> deps = getDepartmentManager().findCompanyDepartments(t, id);
		if (deps != null && !deps.isEmpty()) {
			return new Result<Object>(Exceptions.CODE_ERROR_DELETE, "删除单位失败，因为单位下存在部门，请先删除部门后进行删除。");
		}
		List<U> users = getUserManager().find4Company(t, id);
		if (users != null && !users.isEmpty()) {
			return new Result<Object>(Exceptions.CODE_ERROR_DELETE, "删除单位失败，因为单位下存在用户，请先删除用户后进行删除。");
		}
		try {
			Boolean ret = getComapnyManager().delete(t, id, uuid);
			return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除单位失败");
		} catch (Throwable e) {
			return Exceptions.makeErrorResult(e);
		}
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])修改 [(${domainShowName})]([(${id})])的状态为([(${status})]), TID:[(${tid})]")
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public @ResponseBody Result<Object> changeStatus(@RequestParam(value = "status") int status,
			@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		try {
			Boolean ret = getComapnyManager().updateStatus(t, status, id, uuid);
			return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
		} catch (Throwable e) {
			return Exceptions.makeErrorResult(e);
		}
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
//		try {
//			Boolean ret = getComapnyManager().addLeaders(t, orgId, orgUuid, userUuidIds);
//			return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除对象失败");
//		} catch (Throwable e) {
//			return Exceptions.makeErrorResult(e);
//		}
		return null;
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])删除 [(${domainShowName})]的领导班子成员([(${userUuidIds})]), TID:[(${tid})]")
	@RequestMapping(value = "/deleteLeaders", method = RequestMethod.POST)
	public @ResponseBody Result<Object> deleteLeaders(@RequestParam(value = "orgId") Long orgId,
			@RequestParam(value = "orgUuid") String orgUuid, @RequestBody Map<String, Long> userUuidIds, Trace t) {
//		try {
//			Boolean ret = getComapnyManager().deleteLeaders(t, orgId, orgUuid, userUuidIds);
//			return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除对象失败");
//		} catch (Throwable e) {
//			return Exceptions.makeErrorResult(e);
//		}
		return null;
	}

}
