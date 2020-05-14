package com.linkallcloud.um.pc.controller.party;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.enums.Logical;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.web.perm.RequirePermissions;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/YwDepartment", method = RequestMethod.POST)
@Module(name = "运维部门")
@RequirePermissions({ "sys_org_user-org" })
public class YwDepartmentController {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwDepartmentManager ywDepartmentManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwSystemConfigManager ywSystemConfigManager;

	@RequirePermissions({ "sys_org_user-org_add" })
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass, Trace t, ModelMap modelMap,
			SessionUser su) {
		modelMap.put("companyId", su.companyId());

		Tree zfOrgDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "org_type");
		modelMap.put("zfOrgDicts", zfOrgDictTree != null ? zfOrgDictTree.getChildren() : new ArrayList<Tree>());

//		YwSystemConfig cfg = ywSystemConfigManager.fetchByCompanyId(t, su.companyId());
//		modelMap.put("cfg", cfg == null ? new YwSystemConfig() : cfg);

		return edit(parentId, parentClass, null, null, t, modelMap, su);
	}

	@RequirePermissions({ "sys_org_user-org_edit" })
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap, SessionUser su) {
		modelMap.put("companyId", su.companyId());
		modelMap.put("parentId", parentId);
		modelMap.put("parentClass", parentClass);
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);

		Tree zfOrgDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "org_type");
		modelMap.put("zfOrgDicts", zfOrgDictTree != null ? zfOrgDictTree.getChildren() : new ArrayList<Tree>());

//		YwSystemConfig cfg = ywSystemConfigManager.fetchByCompanyId(t, su.companyId());
//		modelMap.put("cfg", cfg == null ? new YwSystemConfig() : cfg);

		return "page/party/YwDepartment/YwDepartmentEdit";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Result<YwDepartment> get(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, SessionUser su) {
		YwDepartment entity = null;
		if (id != null && id > 0) {// && uuid != null
			// entity = ywDepartmentManager.fetchByIdUuid(t, id, uuid);
			entity = ywDepartmentManager.fetchById(t, id);
		} else {
			entity = new YwDepartment();
			if (!Strings.isBlank(parentClass) && parentClass.equals(YwCompany.class.getSimpleName())) {// 登录用户所在公司下建部门
				entity.setParentId(null);
				entity.setParentClass(null);
			} else {
				entity.setParentId(parentId);
				entity.setParentClass(YwDepartment.class.getSimpleName());
				if (!entity.isTopParent()) {
					YwDepartment parent = ywDepartmentManager.fetchById(t, parentId);
					if (parent != null) {
						entity.setOrgName(parent.getName());
					}
				}
			}
		}
		if (entity.isTopParent()) {
			YwCompany company = ywCompanyManager.fetchById(t, su.companyId());
			if (company != null) {
				entity.setOrgName(company.getName());
			}
		}
		entity.setCompanyId(su.companyId());
		return new Result<YwDepartment>(entity);
	}

	@RequirePermissions(value = { "sys_org_user-org_add", "sys_org_user-org_edit" }, logical = Logical.OR)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Result<Tree> save(@RequestBody @Valid YwDepartment entity, Trace t, SessionUser su) {
		entity.setCompanyId(su.companyId());
		entity.setParentClass(entity.isTopParent() ? null : YwDepartment.class.getSimpleName());
		if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
			ywDepartmentManager.update(t, entity);
		} else {
			Long id = ywDepartmentManager.insert(t, entity);
			entity.setId(id);
		}

		entity = ywDepartmentManager.fetchById(t, entity.getId());
		Tree depTreeNode = entity.toTreeNode();
		if (entity.isTopParent()) {
			depTreeNode.setpId("-" + entity.getCompanyId());
		}
		return new Result<Tree>(depTreeNode);
	}

	@RequirePermissions({ "sys_org_user-org_del" })
	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了部门([(${id})]), TID:[(${tid})]")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t) {
		List<YwDepartment> deps = ywDepartmentManager.findDepartmentsByParentDepartmentId(t, id);
		if (deps != null && !deps.isEmpty()) {
			return new Result<Object>(Exceptions.CODE_ERROR_DELETE, "删除部门失败，因为部门下存在子部门，请先删除子部门后进行删除。");
		}
		List<YwUser> users = ywUserManager.find4Department(t, id);
		if (users != null && !users.isEmpty()) {
			return new Result<Object>(Exceptions.CODE_ERROR_DELETE, "删除部门失败，因为部门下存在用户，请先删除用户后进行删除。");
		}
		Boolean ret = ywDepartmentManager.delete(t, id, uuid);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除对象失败");
	}

	// @WebLog(db = true, desc = "用户([(${visitor.name})])[#
	// th:if=\"${status==0}\"]启用了[/][# th:if=\"${status!=0}\"]停用了[/]菜单([(${id})]),
	// TID:[(${tid})]")
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public @ResponseBody Result<Object> changeStatus(@RequestParam(value = "status") int status,
			@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t) {
		Boolean ret = ywDepartmentManager.updateStatus(t, status, id, uuid);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "更新状态失败");
	}

}
