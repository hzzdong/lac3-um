package com.linkallcloud.um.pc.face;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.sys.Operation;
import com.linkallcloud.um.iapi.sys.IOperationManager;
import com.linkallcloud.web.face.base.BaseFace;

@Controller
@RequestMapping(value = "/face/Operation", method = RequestMethod.POST)
@Module(name = "操作")
public class OperationFace extends BaseFace<Operation, IOperationManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IOperationManager operationManager;

	@Override
	protected IOperationManager manager() {
		return operationManager;
	}

}
