package com.linkallcloud.um.activity.holiday;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.holiday.WorkTime;

public interface IWorkTimeActivity extends IActivity<WorkTime> {

	WorkTime fetchByCompanyId(Trace t, Long companyId);
	Boolean initCompanyWorkTime(Trace t, Long companyId);

}
