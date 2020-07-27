package com.linkallcloud.um.service.holiday;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.holiday.HolidayDate;

public interface IHolidayDateService extends IService<HolidayDate> {

	/**
	 * init default
	 */
	void initDefault(Sid company, int year);

	/**
	 * init wordDay
	 */
	public void initWorkDay(Sid company, int year);

	/**
	 * init baseworkday
	 */
	public void initBaseWorkDay(Sid company, int year);

	/**
	 * init holiday
	 */
	public void initHoliday(Sid company, int year);

	HolidayDate getHoliday(Sid company, Integer day);

	List<HolidayDate> getHolidays(Sid company, String date);
	
	Boolean setHoliday(HolidayDate hd);

}
