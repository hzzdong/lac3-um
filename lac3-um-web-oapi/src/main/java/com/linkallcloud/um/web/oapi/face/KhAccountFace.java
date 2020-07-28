package com.linkallcloud.um.web.oapi.face;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.sys.KhAccount;
import com.linkallcloud.um.iapi.sys.IKhAccountManager;

@Controller
@RequestMapping(value = "/face/KhAccount", method = RequestMethod.POST)
@Module(name = "客户账号")
public class KhAccountFace extends AccountFace<KhAccount, IKhAccountManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhAccountManager khAccountManager;

	@Override
	protected IKhAccountManager manager() {
		return khAccountManager;
	}

}
