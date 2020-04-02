package com.linkallcloud.um.kh.controller.kh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;
import com.linkallcloud.um.kh.controller.party.CompanyTreeController;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping(value = "/SelfKhCompany", method = RequestMethod.POST)
@Module(name = "客户公司")
public class SelfKhCompanyController
		extends CompanyTreeController<KhCompany, IKhCompanyManager, KhUser, IKhUserManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Override
	protected IKhCompanyManager getComapnyManager() {
		return khCompanyManager;
	}

	@Override
	protected IKhUserManager getUserManager() {
		return khUserManager;
	}

	@Override
	protected String getTreeMainPage() {
		return "page/khself/KhCompany/tree";
	}

	@Override
	protected String getTreeEditPage() {
		return "page/khself/KhCompany/edit";
	}

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

	@Override
	protected KhCompany doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		KhCompany entity = null;
		if (id != null && uuid != null) {
			entity = getComapnyManager().fetchByIdUuid(t, id, uuid);
		} else {
			entity = new KhCompany();

			ISessionUser su = Controllers.getSessionUser();
			entity.setParentId(Long.valueOf(su.getCompanyId()));
			entity.setParentClass(getCompanyClass().getSimpleName());
			KhCompany company = getComapnyManager().fetchById(t, Long.valueOf(su.getCompanyId()));
			if (company != null) {
				entity.setOrgName(company.getName());
				entity.setKhTypeCode(company.getKhTypeCode());
			}
		}
		return entity;
	}

	@RequestMapping(value = "/addApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addApps(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, @RequestBody Map<String, Long> appUuidIds,
			Trace t, ModelMap modelMap) {
		Boolean ret = getComapnyManager().addApps(t, id, uuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

	@RequestMapping(value = "/removeApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> deleteApps(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, @RequestBody Map<String, Long> appUuidIds,
			Trace t, ModelMap modelMap) {
		Boolean ret = getComapnyManager().removeApps(t, id, uuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

	@RequestMapping(value = "/comanyAppPerm", method = RequestMethod.GET)
	public String comanyAppPerm(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "appUuid", required = false) String appUuid, Trace t, ModelMap modelMap) {
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		modelMap.put("appId", appId);
		modelMap.put("appUuid", appUuid);
		return "page/khself/KhCompany/appPerm";
	}

	@RequestMapping(value = "/getPermedCompanyAppMenuTree", method = RequestMethod.GET)
	public @ResponseBody Result<Object> getPermedCompanyAppMenuTree(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, Trace t, AppVisitor av) throws IllegalParameterException {
		List<Tree> items = getComapnyManager().findPermedCompanyAppMenus(t, Long.parseLong(av.getCompanyId()), id,
				appId);
		return new Result<Object>(items);
	}

	@RequestMapping(value = "/saveCompanyAppMenuPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveCompanyAppMenuPerm(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = getComapnyManager().saveCompanyAppMenuPerm(t, id, uuid, appId, appUuid, menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

	@RequestMapping(value = "/getPermedCompanyAppAreaTree", method = RequestMethod.GET)
	public @ResponseBody Result<Object> getPermedCompanyAppAreaTree(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid,
			@RequestParam(value = "parentAreaId", required = false) Long parentAreaId, Trace t, AppVisitor av)
			throws IllegalParameterException {
		PermedAreaVo vo = getComapnyManager().findPermedCompanyAppAreas(t, Long.parseLong(av.getCompanyId()), id,
				parentAreaId, appId);
		return new Result<Object>(vo);
	}

	@RequestMapping(value = "/saveCompanyAppAreaPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveCompanyAppAreaPerm(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> uuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = getComapnyManager().saveCompanyAppAreaPerm(t, id, uuid, appId, appUuid, uuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DB, "操作失败！");
	}

}
