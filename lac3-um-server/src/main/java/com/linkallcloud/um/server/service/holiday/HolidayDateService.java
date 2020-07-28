package com.linkallcloud.um.server.service.holiday;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.holiday.IHolidayDateActivity;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.dto.holiday.HolidayVo;
import com.linkallcloud.um.service.holiday.IHolidayDateService;

@Service
@Transactional(rollbackFor = Exception.class)
public class HolidayDateService extends BaseService<HolidayDate, IHolidayDateActivity> implements IHolidayDateService {

	@Autowired
	private IHolidayDateActivity holidayDateActivity;

	@Override
	public IHolidayDateActivity activity() {
		return holidayDateActivity;
	}

	@Transactional(readOnly = false)
	@Override
	public void initDefault(Sid company, int year) {
		activity().initDefault(company, year);
	}

	@Transactional(readOnly = false)
	@Override
	public void initWorkDay(Sid company, int year) {
		activity().initWorkDay(company, year);
	}

	@Transactional(readOnly = false)
	@Override
	public void initBaseWorkDay(Sid company, int year) {
		activity().initBaseWorkDay(company, year);
	}

	@Transactional(readOnly = false)
	@Override
	public void initHoliday(Sid company, int year) {
		activity().initHoliday(company, year);
	}

	@Override
	public HolidayDate getHoliday(Sid company, Integer day) {
		return activity().getHoliday(company, day);
	}

	@Override
	public List<HolidayDate> getHolidays(Sid company, String date) {
		return activity().getHolidays(company, date);
	}

	@Override
	public Boolean setHoliday(HolidayDate hd) {
		return activity().setHoliday(hd);
	}

	@Override
	public HolidayVo getCompanyHoliday(Trace t, Sid company, String date) {
		return activity().getCompanyHoliday(t, company, date);
	}

}
