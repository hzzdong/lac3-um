package com.linkallcloud.um.kh.face;

import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.ParentIdFaceRequest;
import com.linkallcloud.core.face.message.request.RelFaceRequest;
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
		return khCompanyManager.getPermedCompanyOrgs(t, app.getId(), su.id());
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadFullTree", method = RequestMethod.POST)
	public @ResponseBody Object loadKhCompanyFullTree(IdFaceRequest fr, Trace t, SessionUser su) {
		Sid company = su.getCompany();
		if (fr.getId() != null && !Strings.isBlank(fr.getUuid())) {
			company = new Sid(fr.getId(), fr.getUuid());
		}
		Tree root = khCompanyManager.getCompanyFullOrgTree(t, company);
		return root.getChildren();
	}

	@Override
	protected void doSave(Trace t, KhCompany entity, SessionUser su) {
		entity.setParentId(su.companyId());
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

	@Face(simple = true)
	@RequestMapping(value = "/loadCompanyAreaFullTree", method = RequestMethod.POST)
	public @ResponseBody Object loadCompanyAreaFullTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		Tree root = manager().loadCompanyAreaFullTree(t, su.getCompany());
		return root.getChildren();
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadCompanyAreaTree", method = RequestMethod.POST)
	public @ResponseBody Object loadCompanyAreaTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		Tree root = manager().loadCompanyAreaTree(t, su.getCompany());
		return root.getChildren();
	}

	@Face(simple = true)
	@RequestMapping(value = "/getConfigCompanyAreaRootIds", method = RequestMethod.POST)
	public @ResponseBody Object getConfigCompanyAreaRootIds(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		return manager().getConfigCompanyAreaRootIds(t, su.getCompany());
	}

	@Face(simple = true)
	@RequestMapping(value = "/addApps", method = RequestMethod.POST)
	public @ResponseBody Object addApps(RelFaceRequest fr, Trace t, SessionUser su) {
		return manager().addApps(t, fr.getId(), fr.getUuid(), fr.getUuidIds());
	}

	@Face(simple = true)
	@RequestMapping(value = "/removeApp", method = RequestMethod.POST)
	public @ResponseBody Object removeApp(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Map<String, Long> appUuidIds = new HashMap<String, Long>();
		appUuidIds.put(fr.getUuid(), fr.getId());
		return manager().removeApps(t, fr.getParentId(), fr.getParentUuid(), appUuidIds);
	}

}
