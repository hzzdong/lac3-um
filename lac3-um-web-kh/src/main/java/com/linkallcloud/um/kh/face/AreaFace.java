package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/Area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Face(simple = true)
	@RequestMapping(value = "/loadTree4MyCompany", method = RequestMethod.POST)
	public @ResponseBody Object loadTree4MyCompany(ObjectFaceRequest<Object> fr, Trace t, SessionUser suser) {
		Tree root = khCompanyManager.loadCompanyAreaTree(t, suser.getCompany());
		return root.getChildren();
	}

}
