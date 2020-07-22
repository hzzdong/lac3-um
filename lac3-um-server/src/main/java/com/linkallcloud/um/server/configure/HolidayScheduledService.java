package com.linkallcloud.um.server.configure;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.server.utils.DateUtil;
import com.linkallcloud.um.service.holiday.IHolidayDateService;

@Component
public class HolidayScheduledService {
	private static final Log log = Logs.get();

	@Autowired
	private IHolidayDateService holidayDateService;

	/**
	 * 每月25日获取节日信息
	 */
	@Scheduled(cron = "0 0 1 25 * ?")
	public void getWorkDay() {
		Long companyId = 1L;

		Calendar calendar = Calendar.getInstance();
		HolidayDate today = holidayDateService.getHoliday(companyId, DateUtil.getCurDayInt());
		HolidayDate nextday = holidayDateService.getHoliday(companyId, DateUtil.getCurDayInt() + 1);
		if (today == null || nextday == null) {
			holidayDateService.initWorkDay(companyId, calendar.get(Calendar.YEAR));
			return;
		}
		if (today.getStatus() == 3 && nextday.getStatus() != 3) {
			// 如果今天是节假日的最后一天（明天不是节假日)，更新节假日信息
			holidayDateService.initWorkDay(companyId, calendar.get(Calendar.YEAR));
		}
		if (today.getDay() == 25) {
			// 每月的25日，更新节假日信息
			log.info("每月25日凌晨一点,更新节假日信息，如果是12月，同时获取下一年的节日信息");
			holidayDateService.initWorkDay(companyId, calendar.get(Calendar.YEAR));
			if (calendar.get(Calendar.MONTH) == 11) {
				holidayDateService.initWorkDay(companyId, calendar.get(Calendar.YEAR) + 1);
			}
		}
	}

}
