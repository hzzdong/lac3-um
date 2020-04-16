package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhCompany", method = RequestMethod.POST)
@Module(name = "客户单位")
public class KhCompanyFace extends BaseFace<KhCompany, IKhCompanyManager> {

	@Value("${oapi.appcode}")
	private String myAppCode;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Override
	protected IKhCompanyManager manager() {
		return khCompanyManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadTree", method = RequestMethod.POST)
	public @ResponseBody Object loadKhCompanyTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		Application app = applicationManager.fetchByCode(t, myAppCode);
		return khCompanyManager.getPermedCompanyOrgs(t, app.getId(), Long.parseLong(su.getId()));
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadFullTree", method = RequestMethod.POST)
	public @ResponseBody Object loadKhCompanyFullTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		Tree root = khCompanyManager.getCompanyFullOrgTree(t, Long.parseLong(su.getCompanyId()));
		return root.getChildren();
	}

	@Override
	protected void doSave(Trace t, KhCompany entity, SessionUser su) {
		entity.setParentId(Long.valueOf(su.getCompanyId()));
		entity.setParentClass(KhCompany.class.getSimpleName());
		super.doSave(t, entity, su);
	}

	@Override
	protected Object convert(Trace t, String method, FaceRequest fr, KhCompany entity) {
		if (!Strings.isBlank(method) && method.equals("save")) {
			Tree node = entity.toTreeNode();
			node.setId("-" + node.getId());
			node.setpId("-" + node.getpId());
			return node;
		}
		return entity;
	}

}
