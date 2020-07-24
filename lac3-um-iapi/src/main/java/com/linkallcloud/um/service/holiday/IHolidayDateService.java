package com.linkallcloud.um.service.holiday;

import java.util.List;

import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.holiday.HolidayDate;

public interface IHolidayDateService extends IService<HolidayDate> {

	/**
	 * init default
	 */
	void initDefault(Long companyId, int year);

	/**
	 * init wordDay
	 */
	public void initWorkDay(Long companyId, int year);

	/**
	 * init baseworkday
	 */
	public void initBaseWorkDay(Long companyId, int year);

	/**
	 * init holiday
	 */
	public void initHoliday(Long companyId, int year);

	HolidayDate getHoliday(Long companyId, Integer day);

	List<HolidayDate> getHolidays(Long companyId, String date);
	
	Boolean setHoliday(HolidayDate hd);

}
