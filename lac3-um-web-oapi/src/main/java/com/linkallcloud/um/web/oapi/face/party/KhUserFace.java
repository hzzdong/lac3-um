package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.ptj.KhPartTimeJob;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.ptj.IKhPartTimeJobManager;

@Controller
@RequestMapping(value = "/face/KhUser", method = RequestMethod.POST)
@Module(name = "客户用户")
public class KhUserFace extends UserFace<KhUser, IKhUserManager, KhPartTimeJob, IKhPartTimeJobManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhPartTimeJobManager khPartTimeJobManager;

	@Override
	protected IKhUserManager userManager() {
		return khUserManager;
	}

	@Override
	protected IKhPartTimeJobManager ptManager() {
		return khPartTimeJobManager;
	}

}
