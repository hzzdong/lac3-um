package com.linkallcloud.um.server.service.holiday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.holiday.IWorkTimeActivity;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.service.holiday.IWorkTimeService;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkTimeService extends BaseService<WorkTime, IWorkTimeActivity> implements IWorkTimeService {

	@Autowired
	private IWorkTimeActivity workTimeActivity;

	@Override
	public IWorkTimeActivity activity() {
		return workTimeActivity;
	}

	@Override
	public WorkTime fetchByCompanyId(Trace t, Long companyId) {
		return activity().fetchByCompanyId(t, companyId);
	}

	@Override
	public Boolean initCompanyWorkTime(Trace t, Long companyId) {
		return activity().initCompanyWorkTime(t, companyId);
	}

}
