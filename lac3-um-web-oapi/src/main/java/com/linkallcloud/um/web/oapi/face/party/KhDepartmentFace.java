package com.linkallcloud.um.web.oapi.face.party;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;

@Controller
@RequestMapping(value = "/face/KhDepartment", method = RequestMethod.POST)
@Module(name = "客户部门")
public class KhDepartmentFace extends DepartmentFace<KhDepartment, KhUser, IKhDepartmentManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhDepartmentManager khDepartmentManager;

	@Override
	protected IKhDepartmentManager manager() {
		return khDepartmentManager;
	}

}
