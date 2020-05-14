package com.linkallcloud.um.pc.face;

import org.apache.dubbo.config.annotation.Reference;
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
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseTreeFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/Area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaFace extends BaseTreeFace<Area, IAreaManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Override
	protected IAreaManager manager() {
		return areaManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadTree4MyCompany", method = RequestMethod.POST)
	public @ResponseBody Object loadTree4MyCompany(ObjectFaceRequest<Object> fr, Trace t, SessionUser suser) {
		Tree root = ywCompanyManager.loadCompanyAreaTree(t, suser.getCompany());
		return root.getChildren();
	}

//	@Face(simple = true)
//	@RequestMapping(value = "/loadTree", method = RequestMethod.POST)
//	public @ResponseBody Object loadTree(ObjectFaceRequest<String> fr, Trace t, AppVisitor av) {
//		Tree root = manager().getTree(t, false);
//		return Arrays.asList(root);
//	}

	@Override
	protected void doSave(Trace t, Area entity, SessionUser su) {
		if (entity.getParentId() != null && entity.getParentId().longValue() > 0L) {
			Area parent = manager().fetchById(t, entity.getParentId());
			if (parent != null) {
				entity.setFullName(parent.getFullName() + entity.getName());
			}
		}
		if (Strings.isBlank(entity.getFullName())) {
			entity.setFullName(entity.getName());
		}
		super.doSave(t, entity, su);
	}

	@Override
	protected Object convert(Trace t, String method, FaceRequest fr, Area entity) {
		if ("save".equals(method)) {
			return entity.toTreeNode();
		} else {
			return super.convert(t, method, fr, entity);
		}
	}

}
