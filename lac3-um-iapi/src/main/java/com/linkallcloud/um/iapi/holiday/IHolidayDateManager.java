package com.linkallcloud.um.iapi.holiday;

import java.util.List;

import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.holiday.HolidayDate;

public interface IHolidayDateManager extends IManager<HolidayDate> {

	/**
	 * init default
	 */
	void initDefault(Long companyId, int year);

	/**
	 * init wordDay
	 */
	void initWorkDay(Long companyId, int year);

	/**
	 * init baseworkday
	 */
	void initBaseWorkDay(Long companyId, int year);

	/**
	 * init holiday
	 */
	void initHoliday(Long companyId, int year);

	HolidayDate getHoliday(Long companyId, Integer day);

	List<HolidayDate> getHolidays(Long companyId, String date);

	Boolean setHoliday(HolidayDate hd);

}
