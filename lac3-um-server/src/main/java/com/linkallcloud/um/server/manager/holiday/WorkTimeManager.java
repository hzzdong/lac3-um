package com.linkallcloud.um.server.manager.holiday;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.iapi.holiday.IWorkTimeManager;
import com.linkallcloud.um.service.holiday.IWorkTimeService;

@Service(interfaceClass = IWorkTimeManager.class, version = "${dubbo.service.version}")
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
	public WorkTime fetchByCompanyId(Trace t, Long companyId) {
		return service().fetchByCompanyId(t, companyId);
	}

	@Override
	public Boolean initCompanyWorkTime(Trace t, Long companyId) {
		return service().initCompanyWorkTime(t, companyId);
	}

}
