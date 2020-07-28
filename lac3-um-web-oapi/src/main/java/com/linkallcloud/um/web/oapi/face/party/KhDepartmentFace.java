package com.linkallcloud.um.web.oapi.face.party;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.web.face.annotation.Face;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/face/KhDepartment", method = RequestMethod.POST)
@Module(name = "客户部门")
public class KhDepartmentFace {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhDepartmentManager khDepartmentManager;

	@Face(login = false)
	@RequestMapping(value = "/fetchById", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return khDepartmentManager.fetchById(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
	public @ResponseBody Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return khDepartmentManager.fetchByGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody Object find(ListFaceRequest faceReq, Trace t) throws Exception {
		WebQuery wq = faceReq.getQuery();
		if (wq != null) {
			return khDepartmentManager.find(t, wq.toQuery());
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findPage", method = RequestMethod.POST)
	public @ResponseBody Object findPage(PageFaceRequest faceReq, Trace t) throws Exception {
		Page<KhDepartment> page = new Page<>(faceReq);
		return khDepartmentManager.findPage(t, page);
	}

	@Face(login = false)
	@RequestMapping(value = "/findCompanyDepartments", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyDepartments(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return khDepartmentManager.findCompanyDepartments(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/findZfDepartmentsByCompanyGovCode", method = RequestMethod.POST)
	public @ResponseBody Object findZfDepartmentsByCompanyGovCode(ObjectFaceRequest<String> faceReq, Trace t)
			throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return khDepartmentManager.findZfDepartmentsByCompanyGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findZfTopDepartmentsByCompanyGovCode", method = RequestMethod.POST)
	public @ResponseBody Object findZfTopDepartmentsByCompanyGovCode(ObjectFaceRequest<String> faceReq, Trace t)
			throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return khDepartmentManager.findZfTopDepartmentsByCompanyGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findCompanyDirectDepartments", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyDirectDepartments(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return khDepartmentManager.findCompanyDirectDepartments(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/findDirectDepartmentsByParentDepartmentGovCode", method = RequestMethod.POST)
	public @ResponseBody Object findDirectDepartmentsByParentDepartmentGovCode(ObjectFaceRequest<String> faceReq,
			Trace t) throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return khDepartmentManager.findDirectDepartmentsByParentDepartmentGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findDirectDepartmentsByParentDepartmentId", method = RequestMethod.POST)
	public @ResponseBody Object findDirectDepartmentsByParentDepartmentId(IdFaceRequest faceReq, Trace t)
			throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return khDepartmentManager.findDirectDepartmentsByParentDepartmentId(t, faceReq.getId());
	}
}
