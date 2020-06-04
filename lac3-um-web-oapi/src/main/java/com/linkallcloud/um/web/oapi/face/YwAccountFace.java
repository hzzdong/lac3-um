package com.linkallcloud.um.web.oapi.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.sys.YwAccount;
import com.linkallcloud.um.iapi.sys.IYwAccountManager;

@Controller
@RequestMapping(value = "/face/YwAccount", method = RequestMethod.POST)
@Module(name = "运维账号")
public class YwAccountFace extends AccountFace<YwAccount, IYwAccountManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwAccountManager ywAccountManager;

	@Override
	protected IYwAccountManager manager() {
		return ywAccountManager;
	}
}
