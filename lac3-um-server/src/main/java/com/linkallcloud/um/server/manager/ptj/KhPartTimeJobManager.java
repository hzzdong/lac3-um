package com.linkallcloud.um.server.manager.ptj;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.ptj.KhPartTimeJob;
import com.linkallcloud.um.iapi.ptj.IKhPartTimeJobManager;
import com.linkallcloud.um.service.ptj.IKhPartTimeJobService;

@Service(interfaceClass = IKhPartTimeJobManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "兼职")
public class KhPartTimeJobManager extends PartTimeJobManager<KhPartTimeJob, IKhPartTimeJobService>
		implements IKhPartTimeJobManager {

	@Autowired
	private IKhPartTimeJobService khPartTimeJobService;

	@Override
	protected IKhPartTimeJobService service() {
		return khPartTimeJobService;
	}

}
