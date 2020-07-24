package com.linkallcloud.um.server.service.holiday;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.holiday.IHolidayDateActivity;
import com.linkallcloud.um.domain.holiday.HolidayDate;
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
	public void initDefault(Long companyId, int year) {
		activity().initDefault(companyId, year);
	}

	@Transactional(readOnly = false)
	@Override
	public void initWorkDay(Long companyId, int year) {
		activity().initWorkDay(companyId, year);
	}

	@Transactional(readOnly = false)
	@Override
	public void initBaseWorkDay(Long companyId, int year) {
		activity().initBaseWorkDay(companyId, year);
	}

	@Transactional(readOnly = false)
	@Override
	public void initHoliday(Long companyId, int year) {
		activity().initHoliday(companyId, year);
	}

	@Override
	public HolidayDate getHoliday(Long companyId, Integer day) {
		return activity().getHoliday(companyId, day);
	}

	@Override
	public List<HolidayDate> getHolidays(Long companyId, String date) {
		return activity().getHolidays(companyId, date);
	}

	@Override
	public Boolean setHoliday(HolidayDate hd) {
		return activity().setHoliday(hd);
	}

}
