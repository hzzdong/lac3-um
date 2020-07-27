package com.linkallcloud.um.server.activity.holiday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.holiday.IWorkTimeActivity;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.server.dao.holiday.IWorkTimeDao;

@Component
public class WorkTimeActivity extends BaseActivity<WorkTime, IWorkTimeDao> implements IWorkTimeActivity {

	@Autowired
	private IWorkTimeDao workTimeDao;

	@Override
	public IWorkTimeDao dao() {
		return workTimeDao;
	}

	@Override
	public WorkTime fetchByCompanyId(Trace t, Sid company) {
		return dao().fetchByCompanyId(t, company.getId(), company.getCode());
	}

	@Override
	public Boolean initCompanyWorkTime(Trace t, Sid company) {
		WorkTime entity = new WorkTime(company.getId(), company.getCode(), 8, 30, 12, 0, 13, 30, 17, 30);
		return dao().insert(t, entity) > 0;
	}

}
