package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhDepartment", method = RequestMethod.POST)
@Module(name = "客户部门")
public class KhDepartmentFace extends BaseFace<KhDepartment, IKhDepartmentManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhDepartmentManager khDepartmentManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
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

}
