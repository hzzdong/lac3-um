package com.linkallcloud.um.web.oapi.face.party;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.IDepartmentManager;
import com.linkallcloud.web.face.annotation.Face;

public abstract class DepartmentFace<T extends Department, U extends User, TM extends IDepartmentManager<T, U>> {

	protected abstract TM manager();

	@Face(login = false)
	@RequestMapping(value = "/fetchById", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return manager().fetchById(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
	public @ResponseBody Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return manager().fetchByGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody Object find(ListFaceRequest faceReq, Trace t) throws Exception {
		WebQuery wq = faceReq.getQuery();
		if (wq != null) {
			return manager().find(t, wq.toQuery());
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findPage", method = RequestMethod.POST)
	public @ResponseBody Object findPage(PageFaceRequest faceReq, Trace t) throws Exception {
		Page<T> page = new Page<>(faceReq);
		return manager().findPage(t, page);
	}

	@Face(login = false)
	@RequestMapping(value = "/findCompanyDepartments", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyDepartments(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return manager().findCompanyDepartments(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/findZfDepartmentsByCompanyGovCode", method = RequestMethod.POST)
	public @ResponseBody Object findZfDepartmentsByCompanyGovCode(ObjectFaceRequest<String> faceReq, Trace t)
			throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return manager().findZfDepartmentsByCompanyGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findZfTopDepartmentsByCompanyGovCode", method = RequestMethod.POST)
	public @ResponseBody Object findZfTopDepartmentsByCompanyGovCode(ObjectFaceRequest<String> faceReq, Trace t)
			throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return manager().findZfTopDepartmentsByCompanyGovCode(t, govCode);
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findCompanyDirectDepartments", method = RequestMethod.POST)
	public @ResponseBody Object findCompanyDirectDepartments(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return manager().findCompanyDirectDepartments(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/findDirectDepartmentsByParentDepartmentGovCode", method = RequestMethod.POST)
	public @ResponseBody Object findDirectDepartmentsByParentDepartmentGovCode(ObjectFaceRequest<String> faceReq,
			Trace t) throws Exception {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			return manager().findDirectDepartmentsByParentDepartmentGovCode(t, govCode);
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
		return manager().findDirectDepartmentsByParentDepartmentId(t, faceReq.getId());
	}

	@Face(login = false)
	@RequestMapping(value = "/findDepartmentsByParentDepartmentId", method = RequestMethod.POST)
	public @ResponseBody Object findDepartmentsByParentDepartmentId(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return manager().findDepartmentsByParentDepartmentId(t, faceReq.getId());
	}

}
