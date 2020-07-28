package com.linkallcloud.um.iapi.holiday;

import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.dto.holiday.HolidayVo;

public interface IHolidayDateManager extends IManager<HolidayDate> {

	/**
	 * init default
	 */
	void initDefault(Sid company, int year);

	/**
	 * init wordDay
	 */
	void initWorkDay(Sid company, int year);

	/**
	 * init baseworkday
	 */
	void initBaseWorkDay(Sid company, int year);

	/**
	 * init holiday
	 */
	void initHoliday(Sid company, int year);

	HolidayDate getHoliday(Sid company, Integer day);

	List<HolidayDate> getHolidays(Sid company, String date);

	Boolean setHoliday(HolidayDate hd);
	
	/**
	 * 查询休息日和工作时间
	 * 
	 * @param t
	 * @param company
	 * @param date
	 * @return
	 */
	HolidayVo getCompanyHoliday(Trace t, Sid company, String date);

}
