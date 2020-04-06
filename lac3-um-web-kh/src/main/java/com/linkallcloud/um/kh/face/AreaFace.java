package com.linkallcloud.um.kh.face;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/Area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhSystemConfigManager khSystemConfigManager;

	@Face(simple = true)
	@RequestMapping(value = "/loadTree4MyCompany", method = RequestMethod.POST)
	public @ResponseBody Object loadTree4MyCompany(ObjectFaceRequest<Object> fr, Trace t, SessionUser suser) {
		KhSystemConfig sc = khSystemConfigManager.fetchByCompanyId(t, Long.parseLong(suser.getCompanyId()));
		Long areaRootId = 0L;
		if (sc != null && sc.getRootAreaId() != null) {
			areaRootId = sc.getRootAreaId();
		}
		List<Tree> result = areaManager.findChildrenTreeNodes(t, areaRootId, new Equal("status", 0));
		if (result != null && !result.isEmpty()) {
			for (Tree node : result) {
				if (node.getId().equals(areaRootId.toString())) {
					node.setOpen(true);
				}
			}
		}
		return result;
	}

}
