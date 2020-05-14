package com.linkallcloud.um.pc.face;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.um.iapi.sys.IOperationManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/Application", method = RequestMethod.POST)
@Module(name = "应用")
public class ApplicationFace extends BaseFace<Application, IApplicationManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IOperationManager operationManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IMenuManager menuManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Override
	protected IApplicationManager manager() {
		return applicationManager;
	}

	@Override
	protected Object convert(Trace t, String method, FaceRequest fr, Application entity) {
		if (isInnerVisitor(t, fr)) {
			return entity;
		} else {
			if (entity != null && entity.getCode().equals(fr.getAppCode())) {
				entity.desensitization();
				return entity;
			}
		}
		return null;
	}

	@Override
	protected Object convert(Trace t, String method, FaceRequest faceReq, Page<Application> page) {
		if (isInnerVisitor(t, faceReq)) {
			return page;
		} else {
			List<Application> result = new ArrayList<>();
			if (page.getData() != null && page.getData().size() > 0) {
				for (Application app : page.getData()) {
					if (app.getCode().equals(faceReq.getAppCode())) {
						app.desensitization();
						result.add(app);
					}
				}
			}
			page.setData(result);
			return page;
		}
	}

	@Override
	protected Object converts(Trace t, String method, FaceRequest faceReq, List<Application> apps) {
		if (isInnerVisitor(t, faceReq)) {
			return apps;
		} else {
			List<Application> result = new ArrayList<>();
			if (apps != null && apps.size() > 0) {
				for (Application app : apps) {
					if (app.getCode().equals(faceReq.getAppCode())) {
						app.desensitization();
						result.add(app);
					}
				}
			}
			return result;
		}
	}

	/**
	 * 判断是否内部应用访问
	 *
	 * @param t
	 * @param faceReq
	 * @return
	 */
	private boolean isInnerVisitor(Trace t, FaceRequest faceReq) {
		String visitorAppCode = faceReq.getAppCode();
		Application visitorApp = applicationManager.fetchByCode(t, visitorAppCode);
		if (visitorApp != null && visitorApp.getType() == 0) {// 内部应用
			return true;
		}
		return false;
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadAppUriRescodeMap", method = RequestMethod.POST)
	public @ResponseBody Object loadAppUriRescodeMap(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		Map<String, String[]> result = operationManager.loadAppUriRescodeMap(t, faceReq.getId());
		return result;
	}

	@Face(simple = true)
	@RequestMapping(value = "/getAppMenuTree", method = RequestMethod.POST)
	public @ResponseBody Object getAppMenuTree(IdFaceRequest fr, Trace t) throws Exception {
		return menuManager.getMenuTree(t, new Sid(fr.getId(), fr.getUuid()), false);
	}

	@Face(simple = true)
	@RequestMapping(value = "/getAppMenus", method = RequestMethod.POST)
	public @ResponseBody Object getAppMenus(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
		String appCode = faceReq.getData();
		List<Menu> menus = menuManager.getValidMenuList(t, appCode);
		return menus;
	}

	@Face(simple = true)
	@RequestMapping(value = "/findByYwUserId", method = RequestMethod.POST)
	public @ResponseBody Object findByYwUserId(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		if (isInnerVisitor(t, faceReq)) {
			List<Application> apps = applicationManager.find4YwUser(t, faceReq.getId());
			return converts(t, "findByYwUserId", faceReq, apps);
		}
		return null;
	}

	@WebLog(db = true)
	@Face(simple = true)
	@RequestMapping(value = "/saveInter", method = RequestMethod.POST)
	public @ResponseBody Object saveInter(ObjectFaceRequest<Application> fr, Trace t, SessionUser su) {
		Application entity = fr.getData();
		Application dbApp = manager().fetchByIdUuid(t, entity.getId(), entity.getUuid());
		dbApp.setHost(entity.getHost());
		dbApp.setTimeout(entity.getTimeout());
		dbApp.setMessageEncAlg(entity.getMessageEncAlg());
		dbApp.setMessageEncKey(entity.getMessageEncKey());
		dbApp.setSignatureAlg(entity.getSignatureAlg());
		dbApp.setSignatureKey(entity.getSignatureKey());
		manager().updateInterfaceInfo(t, dbApp);
		return convert(t, "save", fr, dbApp);
	}

	@WebLog(db = true)
	@Face(simple = true)
	@RequestMapping(value = "/saveMappingInter", method = RequestMethod.POST)
	public @ResponseBody Object saveMappingInter(ObjectFaceRequest<Application> fr, Trace t, SessionUser su) {
		Application entity = fr.getData();
		Application dbApp = manager().fetchByIdUuid(t, entity.getId(), entity.getUuid());
		dbApp.setAuthAddr(entity.getAuthAddr());
		dbApp.setAuthPassMode(entity.getAuthPassMode());
		dbApp.setAuthSignAlg(entity.getAuthSignAlg());
		dbApp.setAuthSignKey(entity.getAuthSignKey());
		manager().updateMappingInfo(t, dbApp);
		return convert(t, "save", fr, dbApp);
	}

}
