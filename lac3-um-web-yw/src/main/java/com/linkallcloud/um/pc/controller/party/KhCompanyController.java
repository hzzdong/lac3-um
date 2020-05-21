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
import com.linkallcloud.web.controller.BaseLController;
import com.linkallcloud.web.perm.RequirePermissions;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;

@Controller
@RequestMapping(value = "/KhCompany", method = RequestMethod.POST)
@Module(name = "运维公司")
@RequirePermissions({ "customer_qy-manage" })
public class KhCompanyController extends BaseLController<KhCompany, IKhCompanyManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Override
	protected IKhCompanyManager manager() {
		return khCompanyManager;
	}

	@Override
	protected String getMainPage() {
		return "page/party/KhCompany/main";
	}

	@Override
	protected String getEditPage() {
		return "page/party/KhCompany/edit";
	}

	@Override
	protected String getSelectPage() {
		return "page/party/KhCompany/select";
	}

	@Override
	protected Page<KhCompany> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
		Page<KhCompany> page = webPage.toPage();
		page.addRule(new Equal("ywUserId", av.id()));
		page.addRule(new Equal("appId", av.appId()));
		return manager().findPage4Select(t, page);
	}

	@Override
	protected Page<KhCompany> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
		Page<KhCompany> page = webPage.toPage();
		page.addRule(new Equal("ywUserId", av.id()));
		page.addRule(new Equal("appId", av.appId()));
		return manager().findPage(t, page);
	}

	@Override
	protected String doAdd(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
			AppVisitor av) {
		Tree ctoDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_org");
		modelMap.put("ctoDicts", ctoDictTree != null ? ctoDictTree.getChildren() : new ArrayList<Tree>());

		Tree ctpDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_person");
		modelMap.put("ctpDicts", ctpDictTree != null ? ctpDictTree.getChildren() : new ArrayList<Tree>());

		return super.doAdd(prepare, parentId, parentClass, t, modelMap, av);
	}

	@Override
	protected KhCompany doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		KhCompany entity = null;
		if (id != null && uuid != null) {
			entity = manager().fetchByIdUuid(t, id, uuid);
		} else {
			entity = mirror.born();
			entity.setTypeCode("kh_xx");
		}
		return entity;
	}

	@Override
	protected KhCompany doSave(KhCompany entity, Trace t, AppVisitor av) {
		if (entity.getId() != null && entity.getUuid() != null) {
			manager().update(t, entity);
		} else {
			if (Strings.isBlank(entity.getGovCode())) {
				entity.setGovCode(entity.generateUuid());
			}
			Long id = manager().insert(t, entity);
			entity.setId(id);
		}
		return entity;
	}

	@Override
	protected String doEdit(boolean prepare, Long parentId, String parentClass, Long id, String uuid, Trace t,
			ModelMap modelMap, AppVisitor av) {
		Tree ctoDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_org");
		modelMap.put("ctoDicts", ctoDictTree != null ? ctoDictTree.getChildren() : new ArrayList<Tree>());

		Tree ctpDictTree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, "certificate_type_person");
		modelMap.put("ctpDicts", ctpDictTree != null ? ctpDictTree.getChildren() : new ArrayList<Tree>());

		return super.doEdit(prepare, parentId, parentClass, id, uuid, t, modelMap, av);
	}

	@RequestMapping(value = "/addApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> addApps(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, @RequestBody Map<String, Long> appUuidIds,
			Trace t, ModelMap modelMap) {
		Boolean ret = manager().addApps(t, id, uuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

	@RequestMapping(value = "/removeApps", method = RequestMethod.GET)
	public @ResponseBody Result<Object> deleteApps(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, @RequestBody Map<String, Long> appUuidIds,
			Trace t, ModelMap modelMap) {
		Boolean ret = manager().removeApps(t, id, uuid, appUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
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
		return "page/party/KhCompany/appPerm";
	}

	@RequestMapping(value = "/getPermedAppMenuTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> getUmTree(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, Trace t, AppVisitor av) throws IllegalParameterException {
		List<Tree> items = manager().findPermedKhCompanyAppMenus(t, av.companyId(), id, appId);
		return new Result<>(items);
	}

	@RequestMapping(value = "/saveCompanyAppMenuPerm", method = RequestMethod.GET)
	public @ResponseBody Result<Object> saveCompanyAppMenuPerm(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "appId") Long appId,
			@RequestParam(value = "appUuid") String appUuid, @RequestBody Map<String, Long> menuUuidIds, Trace t,
			ModelMap modelMap) {
		Boolean ret = manager().saveCompanyAppMenuPerm(t, id, uuid, appId, appUuid, menuUuidIds);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_UNKNOW, "保存失败");
	}

}
