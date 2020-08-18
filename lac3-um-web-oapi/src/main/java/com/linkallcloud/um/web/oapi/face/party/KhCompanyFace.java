package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;

@Controller
@RequestMapping(value = "/face/KhCompany", method = RequestMethod.POST)
@Module(name = "客户公司")
public class KhCompanyFace extends CompanyFace<KhCompany, KhUser, IKhCompanyManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Override
	protected IKhCompanyManager manager() {
		return khCompanyManager;
	}

}
