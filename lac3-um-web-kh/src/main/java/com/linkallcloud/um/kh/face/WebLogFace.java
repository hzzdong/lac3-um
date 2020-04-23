package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.sys.UmWebLog;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;

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

}
