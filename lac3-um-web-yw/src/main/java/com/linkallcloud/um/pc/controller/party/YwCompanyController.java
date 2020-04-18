package com.linkallcloud.um.pc.controller.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;

@Controller
@RequestMapping(value = "/YwCompany", method = RequestMethod.POST)
@Module(name = "运维公司")
// @RequirePermissions({ "sys_org_user-org" })
public class YwCompanyController extends
		CompanyTreeController<YwCompany, IYwCompanyManager, YwUser, IYwUserManager, YwDepartment, IYwDepartmentManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwDepartmentManager ywDepartmentManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwSystemConfigManager ywSystemConfigManager;

	@Override
	protected IYwCompanyManager getComapnyManager() {
		return ywCompanyManager;
	}

	@Override
	protected IYwUserManager getUserManager() {
		return ywUserManager;
	}

	@Override
	protected IYwDepartmentManager getDepartmentManager() {
		return ywDepartmentManager;
	}

	@Override
	protected String toTree(Trace t, ModelMap modelMap, AppVisitor av) {
		YwSystemConfig cfg = ywSystemConfigManager.fetchByCompanyId(t, av.companyId());
		modelMap.put("cfg", cfg == null ? new YwSystemConfig() : cfg);
		return super.toTree(t, modelMap, av);
	}

	// @RequirePermissions({ "sys_org_user-org_addCompany" })
	@Override
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass, Trace t, ModelMap modelMap) {
		Tree ctoDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_org");
		modelMap.put("ctoDicts", ctoDictTree != null ? ctoDictTree.getChildren() : new ArrayList<Tree>());

		Tree ctpDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_person");
		modelMap.put("ctpDicts", ctpDictTree != null ? ctpDictTree.getChildren() : new ArrayList<Tree>());

		return super.add(parentId, parentClass, t, modelMap);
	}

	// @RequirePermissions({ "sys_org_user-org_edit" })
	@Override
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "parentClass", required = false) String parentClass,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		Tree ctoDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_org");
		modelMap.put("ctoDicts", ctoDictTree != null ? ctoDictTree.getChildren() : new ArrayList<Tree>());

		Tree ctpDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_person");
		modelMap.put("ctpDicts", ctpDictTree != null ? ctpDictTree.getChildren() : new ArrayList<Tree>());

		return super.edit(parentId, parentClass, id, uuid, t, modelMap);
	}

	// @RequirePermissions(value = { "sys_org_user-org_addCompany",
	// "sys_org_user-org_edit" }, logical = Logical.OR)
	@Override
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Result<Tree> save(@RequestBody YwCompany entity, Trace t, AppVisitor av) {
		if (Strings.isBlank(entity.getGovCode())) {
			entity.setGovCode(entity.generateUuid());
		}
		return super.save(entity, t, av);
	}

	// @RequirePermissions({ "sys_org_user-org_del" })
	@Override
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		return super.delete(id, uuid, t, av);
	}

	@RequestMapping(value = "/comanyAppPerm", method = RequestMethod.GET)
	public String comanyAppPerm(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return "page/party/YwCompany/appPerm";
	}

	@RequestMapping(value = "/getPermedCompanyAppMenuTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> getPermedCompanyAppMenuTree(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) throws IllegalParameterException {
		List<Tree> items = getComapnyManager().findPermedCompanyAppMenus(t, av.companyId(), id,
				av.appId());
		return new Result<List<Tree>>(items);
	}

	@RequestMapping(value = "/saveCompanyAppMenuPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveCompanyAppMenuPerm(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			AppVisitor av) {
		Boolean ret = getComapnyManager().saveCompanyAppMenuPerm(t, id, uuid, av.appId(),
				av.appUuid(), menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/getPermedCompanyAppAreaTree", method = RequestMethod.GET)
	public @ResponseBody Result<PermedAreaVo> getPermedCompanyAppAreaTree(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "parentAreaId", required = false) Long parentAreaId, Trace t, AppVisitor av)
			throws IllegalParameterException {
		PermedAreaVo pavo = getComapnyManager().findPermedCompanyAppAreas(t, av.companyId(), id,
				parentAreaId, av.appId());
		return new Result<PermedAreaVo>(pavo);
	}

	@RequestMapping(value = "/saveCompanyAppAreaPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveCompanyAppAreaPerm(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
		Boolean ret = getComapnyManager().saveCompanyAppAreaPerm(t, id, uuid, av.appId(),
				av.appUuid(), uuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

}
