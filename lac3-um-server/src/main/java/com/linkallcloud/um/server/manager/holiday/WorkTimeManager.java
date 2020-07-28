package com.linkallcloud.um.server.manager.holiday;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.iapi.holiday.IWorkTimeManager;
import com.linkallcloud.um.service.holiday.IWorkTimeService;

@DubboService(interfaceClass = IWorkTimeManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "工作时间")
public class WorkTimeManager extends BaseManager<WorkTime, IWorkTimeService> implements IWorkTimeManager {

	@Autowired
	private IWorkTimeService workTimeService;

	@Override
	protected IWorkTimeService service() {
		return workTimeService;
	}

	@Override
	public WorkTime fetchByCompanyId(Trace t, Sid company) {
		return service().fetchByCompanyId(t, company);
	}

	@Override
	public Boolean initCompanyHolidayWorkTime(Trace t, Sid company) {
		return service().initCompanyHolidayWorkTime(t, company);
	}

}
