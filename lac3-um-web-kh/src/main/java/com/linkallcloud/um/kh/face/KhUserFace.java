package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhUser", method = RequestMethod.POST)
@Module(name = "客户用户")
public class KhUserFace {
	
	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhUserManager khUserManager;

	@Face(simple = true)
	@RequestMapping(value = "/fetchLoginUser", method = RequestMethod.POST)
	public @ResponseBody Object fetchLoginUser(ObjectFaceRequest<Object> fr, SessionUser suser) {
		return suser;
	}

}
