package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.ptj.YwPartTimeJob;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.ptj.IYwPartTimeJobManager;

@Controller
@RequestMapping(value = "/face/YwUser", method = RequestMethod.POST)
@Module(name = "运维用户")
public class YwUserFace extends UserFace<YwUser, IYwUserManager, YwPartTimeJob, IYwPartTimeJobManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwPartTimeJobManager ywPartTimeJobManager;

	@Override
	protected IYwUserManager userManager() {
		return ywUserManager;
	}

	@Override
	protected IYwPartTimeJobManager ptManager() {
		return ywPartTimeJobManager;
	}

}
