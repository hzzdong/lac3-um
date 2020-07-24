package com.linkallcloud.um.iapi.holiday;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.holiday.WorkTime;

public interface IWorkTimeManager extends IManager<WorkTime> {
	
	WorkTime fetchByCompanyId(Trace t, Long companyId);

	Boolean initCompanyWorkTime(Trace t, Long companyId);

}
