package com.linkallcloud.um.server.activity.holiday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.um.activity.holiday.IHolidayDateActivity;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.server.dao.holiday.IHolidayDateDao;
import com.linkallcloud.um.server.utils.DateTimeUtils;
import com.linkallcloud.um.server.utils.DateUtil;
import com.linkallcloud.um.server.utils.HolidayUtil;

@Component
public class HolidayDateActivity extends BaseActivity<HolidayDate, IHolidayDateDao> implements IHolidayDateActivity {

	@Autowired
	private IHolidayDateDao holidayDateDao;

	@Override
	public IHolidayDateDao dao() {
		return holidayDateDao;
	}

	@Override
	public void initDefault(Long companyId, int year) {
		initWeek(companyId, year);
	}

	private boolean initWeek(Long companyId, int year) {
		Calendar start = Calendar.getInstance();
		start.set(year, 0, 1);
		List<HolidayDate> list = new ArrayList<>();
		while (start.get(Calendar.YEAR) == year && start.get(Calendar.MONTH) == 0) {
			HolidayDate date = new HolidayDate();
			date.setCompanyId(companyId);
			date.setYear(start.get(Calendar.YEAR));
			date.setMonth(start.get(Calendar.MONTH) + 1);
			date.setDay(start.get(Calendar.DAY_OF_MONTH));

			int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == 1 || dayOfWeek == 7) {
				date.setStatus(1);
			} else {
				date.setStatus(0);
			}
			log.info(date);
			start.add(Calendar.DAY_OF_YEAR, 1);
			list.add(date);
		}
		try {
			holidayDateDao.batchInsertHolidayDate(list);
			return true;
		} catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Init work day. 生成当年的工作日信息
	 * 
	 * @param year the year
	 */
	@Override
	public void initWorkDay(Long companyId, int year) {
		HolidayDate holidayDate = new HolidayDate();
		holidayDate.setCompanyId(companyId);
		String date = year + "-10" + "-01";
		holidayDate.setDate(date);
		HolidayDate result = holidayDateDao.queryHolidayDateLimit1(holidayDate);
		if (result == null) {
			initBaseWorkDay(companyId, year);
			initHoliday(companyId, year);
		} else if (result.getStatus() != 3) {
			// 时光荏苒，但是十月一日一定是法定节假日
			initHoliday(companyId, year);
		}
	}

	/**
	 * 生成周一到周五的工作日周末的非工作日
	 *
	 * @param year the year
	 */
	@Override
	public void initBaseWorkDay(Long companyId, int year) {
		Calendar start = Calendar.getInstance();
		start.set(year, 0, 1);
		List<HolidayDate> list = new ArrayList<>();

		while (start.get(Calendar.YEAR) == year) {
			HolidayDate date = new HolidayDate();
			date.setCompanyId(companyId);
			int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
			int month = start.get(Calendar.MONTH) + 1;
			int day = start.get(Calendar.DAY_OF_MONTH);
			String formatDateStr = DateTimeUtils.getFormatDateStr(start.getTime(), "yyyy-MM-dd");
			date.setDate(formatDateStr);
			date.setYear(year);
			date.setMonth(month);
			date.setDay(day);
			if (dayOfWeek == 1 || dayOfWeek == 7) {
				date.setStatus(1);
			} else {
				date.setStatus(0);
			}
			list.add(date);

			start.add(Calendar.DAY_OF_YEAR, 1);
		}

		holidayDateDao.batchInsertHolidayDate(list);
	}

	/**
	 * 生成节假日，及节假日补班
	 *
	 * @param year the year
	 */
	@Override
	public void initHoliday(Long companyId, int year) {
		List<HolidayDate> allList = new ArrayList<>();
		for (int month = 1; month < 13; month++) {
			List<HolidayDate> monthList = initMonth(companyId, year, month);
			allList.addAll(monthList);
		}
		List<HolidayDate> monthList = initMonth(companyId, year - 1, 12);
		allList.addAll(monthList);
		holidayDateDao.batchInsertHolidayDate(allList);
	}

	@Override
	public HolidayDate getHoliday(Long companyId, Integer day) {
		HolidayDate date = new HolidayDate();
		date.setCompanyId(companyId);
		date.setDate(DateUtil.formatDay(day));
		return holidayDateDao.queryHolidayDateLimit1(date);
	}

	@Override
	public List<HolidayDate> getHolidays(Long companyId, String date) {
		HolidayDate record = new HolidayDate();
		record.setCompanyId(companyId);
		String[] split = date.split("-");
		if (split.length == 1) {
			record.setYear(Integer.parseInt(split[0]));
		} else if (split.length == 2) {
			record.setYear(Integer.parseInt(split[0]));
			record.setMonth(Integer.parseInt(split[1]));
		} else if (split.length == 3) {
			record.setDate(date);
		}
		return holidayDateDao.queryHolidayDate(record);
	}

	/**
	 * Init month list. 按月爬取节假日
	 * 
	 * @param year  the year
	 * @param month the month
	 * @return the list
	 */
	public List<HolidayDate> initMonth(Long companyId, int year, int month) {
		List<HolidayDate> holidayDateList = new ArrayList<>();
		try {
			String result = HolidayUtil.getMonth(year + "", month + "");
			JSONObject json = JSON.parseObject(result);
			System.out.println(result);
			JSONArray data = json.getJSONArray("data");
			JSONObject dataObj = JSON.parseObject(data.get(0).toString());
			JSONArray holidays = dataObj.getJSONArray("holiday");
			for (Object obj : holidays) {
				JSONObject holiday = JSON.parseObject(obj.toString());
				JSONArray list = holiday.getJSONArray("list");
				for (Object dateObj : list) {
					JSONObject date = JSON.parseObject(dateObj.toString());
					HolidayDate holidayDate = parseDate(date);
					holidayDate.setCompanyId(companyId);
					holidayDateList.add(holidayDate);
				}

			}
		} catch (ClassCastException classCastException) {
			log.info("可能是当前月份(" + month + "月)没有节日");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return holidayDateList;
	}

	private HolidayDate parseDate(JSONObject date) {
		HolidayDate holidayDate = new HolidayDate();
		String jsonDate = date.get("date").toString();
		String[] split = jsonDate.split("-");
		int year = Integer.parseInt(split[0]);
		int month = Integer.parseInt(split[1]);
		int day = Integer.parseInt(split[2]);
		holidayDate.setYear(year);
		holidayDate.setMonth(month);
		holidayDate.setDay(day);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		String formatDateStr = DateTimeUtils.getFormatDateStr(calendar.getTime(), "yyyy-MM-dd");
		holidayDate.setDate(formatDateStr);
		// 1法定节假日2补班
		String jsonStatus = date.get("status").toString();
		if ("1".equals(jsonStatus)) {
			holidayDate.setStatus(3);
		} else {
			holidayDate.setStatus(2);
		}
		return holidayDate;

	}

	@Override
	public Boolean setHoliday(HolidayDate hd) {
		return dao().updateHolidayDate(hd) > 0;
	}

}
