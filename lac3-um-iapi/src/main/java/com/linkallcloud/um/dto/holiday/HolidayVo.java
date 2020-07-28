package com.linkallcloud.um.dto.holiday;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.vo.Vo;

/**
 * 假日VO
 * 
 * @author hzzdong
 *
 */
public class HolidayVo extends Vo {
	private static final long serialVersionUID = 5930826286001450378L;

	/**
	 * 日期yyyy-MM-dd
	 */
	private List<String> holidayDates;

	private WorkTimeVo worktime;

	public HolidayVo() {
		super();
	}

	public List<String> getHolidayDates() {
		return holidayDates;
	}

	public void setHolidayDates(List<String> holidayDates) {
		this.holidayDates = holidayDates;
	}
	
	public void addHolidayDate(String holidayDate) {
		if(!Strings.isBlank(holidayDate)) {
			if(holidayDates == null) {
				holidayDates = new ArrayList<>();
			}
			holidayDates.add(holidayDate);
		}
	}

	public WorkTimeVo getWorktime() {
		return worktime;
	}

	public void setWorktime(WorkTimeVo worktime) {
		this.worktime = worktime;
	}

}
