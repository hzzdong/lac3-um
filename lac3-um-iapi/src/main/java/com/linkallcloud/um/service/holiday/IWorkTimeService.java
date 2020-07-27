package com.linkallcloud.um.service.holiday;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.holiday.WorkTime;

public interface IWorkTimeService extends IService<WorkTime> {
	
	WorkTime fetchByCompanyId(Trace t, Sid company);
	Boolean initCompanyHolidayWorkTime(Trace t, Sid company);

}
