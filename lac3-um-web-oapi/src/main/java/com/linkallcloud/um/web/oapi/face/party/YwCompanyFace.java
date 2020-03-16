package com.linkallcloud.um.web.oapi.face.party;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;

@Controller
@RequestMapping(value = "/face/YwCompany", method = RequestMethod.POST)
@Module(name = "运维公司")
public class YwCompanyFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Face(login = false)
	@RequestMapping(value = "/fetchById", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(IdFaceRequest faceReq, Trace t) throws Exception {
		if (Strings.isBlank(faceReq.getId())) {
			return null;
		}
		return ywCompanyManager.fetchById(t, Long.parseLong(faceReq.getId()));
	}

	@Face(login = false)
	@RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
	public @ResponseBody Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
		String govCode = faceReq.getData();
		return ywCompanyManager.fetchByGovCode(t, govCode);
	}

	@Face(login = false)
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody Object find(ListFaceRequest faceReq, Trace t) throws Exception {
		WebQuery wq = faceReq.getQuery();
		if (wq != null) {
			return ywCompanyManager.find(t, wq.toQuery());
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findPage", method = RequestMethod.POST)
	public @ResponseBody Object findPage(PageFaceRequest faceReq, Trace t) throws Exception {
		Page<YwCompany> page = new Page<>(faceReq);
		return ywCompanyManager.findPage(t, page);
	}

	@Face(login = false)
	@RequestMapping(value = "/findSubCompanies", method = RequestMethod.POST)
	public @ResponseBody Object findSubCompanies(IdFaceRequest faceReq, Trace t) throws Exception {
		if (Strings.isBlank(faceReq.getId())) {
			return null;
		}
		return ywCompanyManager.findSubCompanies(t, Long.parseLong(faceReq.getId()));
	}

	@Face(login = false)
	@RequestMapping(value = "/getCompanyFullOrgTreeList", method = RequestMethod.POST)
	public @ResponseBody Object getCompanyFullOrgTreeList(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return ywCompanyManager.getCompanyFullOrgTreeList(t, Long.parseLong(faceReq.getId()));
	}

	@Face(login = false)
	@RequestMapping(value = "/getCompanyFullOrgTreeByGovCode", method = RequestMethod.POST)
	public @ResponseBody Object getCompanyFullOrgTreeByGovCode(ObjectFaceRequest<String> faceReq, Trace t)
			throws Exception {
		if (Strings.isBlank(faceReq.getData())) {
			return null;
		}
		return ywCompanyManager.getCompanyFullOrgTreeByGovCode(t, faceReq.getData());
	}

}
