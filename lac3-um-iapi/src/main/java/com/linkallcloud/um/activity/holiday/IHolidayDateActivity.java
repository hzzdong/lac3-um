package com.linkallcloud.um.activity.holiday;

import java.util.List;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.dto.holiday.HolidayVo;

public interface IHolidayDateActivity extends IActivity<HolidayDate> {

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
	
	/**
	 * 查询休息日和工作时间
	 * 
	 * @param company
	 * @param date
	 * @return
	 */
	HolidayVo getCompanyHoliday(Trace t, Sid company, String date);

}
