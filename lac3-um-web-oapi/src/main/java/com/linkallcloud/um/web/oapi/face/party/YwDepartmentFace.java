package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;

@Controller
@RequestMapping(value = "/face/YwDepartment", method = RequestMethod.POST)
@Module(name = "运维部门")
public class YwDepartmentFace extends DepartmentFace<YwDepartment, YwUser, IYwDepartmentManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwDepartmentManager ywDepartmentManager;

	@Override
	protected IYwDepartmentManager manager() {
		return ywDepartmentManager;
	}

}
