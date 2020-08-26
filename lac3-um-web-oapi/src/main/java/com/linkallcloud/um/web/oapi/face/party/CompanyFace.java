package com.linkallcloud.um.web.oapi.face.party;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.face.TypedOrgTreeRequest;
import com.linkallcloud.um.iapi.party.ICompanyManager;
import com.linkallcloud.web.face.annotation.Face;

public abstract class CompanyFace<T extends Company, U extends User, TM extends ICompanyManager<T, U>> {

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
		return manager().fetchByGovCode(t, govCode);
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
	@RequestMapping(value = "/findSubCompanies", method = RequestMethod.POST)
	public @ResponseBody Object findSubCompanies(IdFaceRequest faceReq, Trace t) throws Exception {
		if (faceReq.getId() == null) {
			return null;
		}
		return manager().findSubCompanies(t, faceReq.getId());
	}

	/**
	 * 根据treeType得到companyId的机构树
	 * 
	 * @param fr, type见com.linkallcloud.um.constant.Consts
	 * @param t
	 * @return
	 */
	@Face(login = false)
	@RequestMapping(value = "/getCompanyTree", method = RequestMethod.POST)
	public @ResponseBody Object getCompanyTree(IdFaceRequest fr, Trace t) throws Exception {
		if (fr.getId() == null || Strings.isBlank(fr.getUuid())) {
			return null;
		}
		Sid company = new Sid(fr.getId(), fr.getUuid());
		return manager().getCompanyTree(t, fr.getType(), company);
	}

	@Face(login = false)
	@RequestMapping(value = "/getTypedCompanyTree", method = RequestMethod.POST)
	public @ResponseBody Object getTypedCompanyTree(TypedOrgTreeRequest fr, Trace t) throws Exception {
		if (fr.getCompanyId() == null) {
			return null;
		}
		return manager().getTypedCompanyTree(t, fr.getCompanyId(), fr.getTypes());
	}
	
	/**
	 * 公司全局根区域的ids。由公司管理员在系统设置中设定。若未设定，默认同父公司。顶层公司未设定为系统全区域。
	 * 
	 * @param fr
	 * @param t
	 * @return
	 * @throws Exception
	 */
	@Face(login = false)
	@RequestMapping(value = "/getCompanyAreaRootIds", method = RequestMethod.POST)
	public @ResponseBody Object getCompanyAreaRootIds(IdFaceRequest fr, Trace t) throws Exception {
		if (fr.getId() == null) {
			return null;
		}
		return manager().getCompanyAreaRootIds(t, fr.getId());
	}

}
