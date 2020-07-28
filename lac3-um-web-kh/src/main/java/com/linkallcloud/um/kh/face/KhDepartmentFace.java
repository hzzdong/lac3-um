package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseTreeFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhDepartment", method = RequestMethod.POST)
@Module(name = "客户部门")
public class KhDepartmentFace extends BaseTreeFace<KhDepartment, IKhDepartmentManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhDepartmentManager khDepartmentManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Override
	protected IKhDepartmentManager manager() {
		return khDepartmentManager;
	}

	public KhDepartmentFace() {
		super();
	}

	@Override
	protected void doSave(Trace t, KhDepartment entity, SessionUser su) {
		entity.setCompanyId(su.companyId());
		if (!entity.getParentClass().equals(KhDepartment.class.getSimpleName()) || entity.getParentId() == null
				|| entity.getParentId().longValue() == 0) {
			entity.setParentClass(null);
			entity.setParentId(0L);
		}
		super.doSave(t, entity, su);
	}

	@Override
	protected Object convert(Trace t, String method, FaceRequest fr, KhDepartment entity) {
		if (!Strings.isBlank(method) && method.equals("save")) {
			Tree node = entity.toTreeNode();
			node.setId(node.getId());
			if (entity.getParentClass() != null && entity.getParentClass().equals(KhCompany.class.getSimpleName())) {
				node.setpId("-" + node.getpId());
			}
			return node;
		}
		return entity;
	}

	@Override
	public Object fetchParent(IdFaceRequest faceReq, Trace t, SessionUser su) {
		return fetchParentOrg(faceReq, t, su);
	}

	private Org fetchParentOrg(IdFaceRequest faceReq, Trace t, SessionUser su) {
		KhDepartment child = this.doFetch(t, faceReq.getId(), faceReq.getUuid(), su);
		if (child != null) {
			if (child.isTopParent()) {
				return khCompanyManager.fetchById(t, child.getCompanyId());
			} else {
				return manager().fetchById(t, child.getParentId());
			}
		}
		return null;
	}

	@Override
	public Object fetchParentTreeNode(IdFaceRequest faceReq, Trace t, SessionUser su) {
		Org parent = fetchParentOrg(faceReq, t, su);
		if (parent != null) {
			return parent.toTreeNode();
		}
		return null;
	}
	

	@Face(simple = true)
	@RequestMapping(value = "/leaderPage", method = RequestMethod.POST)
	public @ResponseBody Object page(PageFaceRequest pfr, Trace t, AppVisitor av) {
		Page<KhUser> page = new Page<KhUser>(pfr);
		return manager().leaderPage(t, page);
	}

	@Face(simple = true)
	@LacLog(desc = "用户([(${su.sid.name})])添加 [(${domainShowName})]机构领导([(${fr.data.userId})]), TID:[(${tid})]")
	@RequestMapping(value = "/addLeader", method = RequestMethod.POST)
	public @ResponseBody Object addLeaders(ObjectFaceRequest<Rel4OrgLeader> fr, Trace t) {
		Rel4OrgLeader leader = fr.getData();
		return manager().addLeader(t, leader);
	}

	@Face(simple = true)
	@LacLog(desc = "用户([(${su.sid.name})])删除 [(${domainShowName})]的机构领导([(${fr.data.userId})]), TID:[(${tid})]")
	@RequestMapping(value = "/deleteLeader", method = RequestMethod.POST)
	public @ResponseBody Object deleteLeaders(ObjectFaceRequest<Rel4OrgLeader> fr, Trace t) {
		Rel4OrgLeader leader = fr.getData();
		return manager().deleteLeader(t, leader);
	}


}
