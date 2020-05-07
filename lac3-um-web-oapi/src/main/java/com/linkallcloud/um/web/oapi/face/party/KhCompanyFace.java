package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.web.face.annotation.Face;

@Controller
@RequestMapping(value = "/face/KhCompany", method = RequestMethod.POST)
@Module(name = "客户公司")
public class KhCompanyFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Face(login = false)
	@RequestMapping(value = "/fetchById", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() != null) {
			return null;
		}
		return khCompanyManager.fetchById(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
	public @ResponseBody Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
		String govCode = faceReq.getData();
		return khCompanyManager.fetchByGovCode(t, govCode);
	}

	@Face(login = false)
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody Object find(ListFaceRequest faceReq, Trace t) throws Exception {
		WebQuery wq = faceReq.getQuery();
		if (wq != null) {
			return khCompanyManager.find(t, wq.toQuery());
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findPage", method = RequestMethod.POST)
	public @ResponseBody Object findPage(PageFaceRequest faceReq, Trace t) throws Exception {
		Page<KhCompany> page = new Page<>(faceReq);
		return khCompanyManager.findPage(t, page);
	}

	@Face(login = false)
	@RequestMapping(value = "/findSubCompanies", method = RequestMethod.POST)
	public @ResponseBody Object findSubCompanies(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() != null) {
			return null;
		}
		return khCompanyManager.findSubCompanies(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/getCompanyTree", method = RequestMethod.POST)
	public @ResponseBody Object getCompanyTree(IdFaceRequest fr, Trace t) throws Exception {
		if (fr.getId() == null || Strings.isBlank(fr.getUuid())) {
			return null;
		}
		Sid company = new Sid(fr.getId(), fr.getUuid());
		return khCompanyManager.getCompanyTree(t, fr.getType(), company);
	}

}
