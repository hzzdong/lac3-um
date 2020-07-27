package com.linkallcloud.um.server.service.holiday;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.holiday.IHolidayDateActivity;
import com.linkallcloud.um.activity.holiday.IWorkTimeActivity;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.service.holiday.IWorkTimeService;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkTimeService extends BaseService<WorkTime, IWorkTimeActivity> implements IWorkTimeService {

	@Autowired
	private IWorkTimeActivity workTimeActivity;

	@Autowired
	private IHolidayDateActivity holidayDateActivity;
	
	@Autowired
	private IYwCompanyActivity ywCompanyActivity;
	
	@Autowired
	private IKhCompanyActivity khCompanyActivity;

	@Override
	public IWorkTimeActivity activity() {
		return workTimeActivity;
	}

	@Override
	public WorkTime fetchByCompanyId(Trace t, Sid company) {
		return activity().fetchByCompanyId(t, company);
	}

	@Override
	public Boolean initCompanyHolidayWorkTime(Trace t, Sid company) {
		Boolean ok = activity().initCompanyWorkTime(t, company);
		if (ok) {
			Calendar calendar = Calendar.getInstance();
			holidayDateActivity.initWorkDay(company, calendar.get(Calendar.YEAR));
			updateChildrenCompaniesHolidaySetup(t, company);
			return true;
		}
		return false;
	}

	private void updateChildrenCompaniesHolidaySetup(Trace t, Sid company) {
		if(YwCompany.class.getSimpleName().equals(company.getCode())) {
			ywCompanyActivity.updateChildrenCompaniesHolidaySetup(t, company.getId());
		} else {
			khCompanyActivity.updateChildrenCompaniesHolidaySetup(t, company.getId());
		}
	}

}
