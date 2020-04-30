package com.linkallcloud.um.server.manager.ptj;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.ptj.YwPartTimeJob;
import com.linkallcloud.um.iapi.ptj.IYwPartTimeJobManager;
import com.linkallcloud.um.service.ptj.IYwPartTimeJobService;

@Service(interfaceClass = IYwPartTimeJobManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "兼职")
public class YwPartTimeJobManager extends PartTimeJobManager<YwPartTimeJob, IYwPartTimeJobService>
		implements IYwPartTimeJobManager {

	@Autowired
	private IYwPartTimeJobService ywPartTimeJobService;

	@Override
	protected IYwPartTimeJobService service() {
		return ywPartTimeJobService;
	}

}
