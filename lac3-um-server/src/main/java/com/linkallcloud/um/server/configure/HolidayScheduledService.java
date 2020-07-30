package com.linkallcloud.um.server.configure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.server.utils.DateUtil;
import com.linkallcloud.um.service.holiday.IHolidayDateService;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.party.IYwCompanyService;

@Component
public class HolidayScheduledService {
	private static final Log log = Logs.get();

	@Autowired
	private IHolidayDateService holidayDateService;

	@Autowired
	private IYwCompanyService ywCompanyService;

	@Autowired
	private IKhCompanyService khCompanyService;

	/**
	 * 每月25日获取节日信息
	 */
	@Scheduled(cron = "0 0 1 25 * ?")
	public void getWorkDay() {
		List<Sid> companies = getAllConfigedCompanies();
		if (companies != null && !companies.isEmpty()) {
			for (Sid company : companies) {
				Calendar calendar = Calendar.getInstance();
				HolidayDate today = holidayDateService.getHoliday(company, DateUtil.getCurDayInt());
				HolidayDate nextday = holidayDateService.getHoliday(company, DateUtil.getCurDayInt() + 1);
				if (today == null || nextday == null) {
					holidayDateService.initWorkDay(company, calendar.get(Calendar.YEAR));
					return;
				}
				if (today.getStatus() == 3 && nextday.getStatus() != 3) {
					// 如果今天是节假日的最后一天（明天不是节假日)，更新节假日信息
					holidayDateService.initWorkDay(company, calendar.get(Calendar.YEAR));
				}
				if (today.getDay() == 25) {
					// 每月的25日，更新节假日信息
					log.info("每月25日凌晨一点,更新节假日信息，如果是12月，同时获取下一年的节日信息");
					holidayDateService.initWorkDay(company, calendar.get(Calendar.YEAR));
					if (calendar.get(Calendar.MONTH) == 11) {
						holidayDateService.initWorkDay(company, calendar.get(Calendar.YEAR) + 1);
					}
				}
			}
		}
	}

	private List<Sid> getAllConfigedCompanies() {
		List<Sid> companies = new ArrayList<>();
		Trace t = new Trace(true);

		Query q = new Query();
		q.addRule(new Equal("status", 0));

		List<YwCompany> ywCompanies = ywCompanyService.find(t, q);
		if (ywCompanies != null && !ywCompanies.isEmpty()) {
			for (YwCompany com : ywCompanies) {
				if (com.getHolidayId() != null && com.getHolidayId().equals(com.getId())) {
					companies.add(new Sid(com.getId(), com.getUuid(), com.getClass().getSimpleName(), com.getName()));
				}
			}
		}

		List<KhCompany> khCompanies = khCompanyService.find(t, q);
		if (khCompanies != null && !khCompanies.isEmpty()) {
			for (KhCompany com : khCompanies) {
				if (com.getHolidayId() != null && com.getHolidayId().equals(com.getId())) {
					companies.add(new Sid(com.getId(), com.getUuid(), com.getClass().getSimpleName(), com.getName()));
				}
			}
		}

		return companies;
	}

}
