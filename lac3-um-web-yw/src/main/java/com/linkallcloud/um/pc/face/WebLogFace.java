package com.linkallcloud.um.pc.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.sys.UmWebLog;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;
import com.linkallcloud.web.face.base.BaseWebLogFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/WebLog", method = RequestMethod.POST)
@Module(name = "日志")
public class WebLogFace extends BaseWebLogFace<UmWebLog, IUmWebLogManager> {
	
	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IUmWebLogManager umWebLogManager;

	@Override
	protected IUmWebLogManager manager() {
		return umWebLogManager;
	}

	@Override
	protected Page<UmWebLog> doPage(Trace t, Page<UmWebLog> page, SessionUser su) {
		page.addRule(new Equal("orgId", su.companyId()));
        page.addRule(new Equal("orgType", su.getUserType()));
		return super.doPage(t, page, su);
	}

}
