package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;

@Controller
@RequestMapping(value = "/face/YwCompany", method = RequestMethod.POST)
@Module(name = "运维公司")
public class YwCompanyFace extends CompanyFace<YwCompany, YwUser, IYwCompanyManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Override
	protected IYwCompanyManager manager() {
		return ywCompanyManager;
	}

}
