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
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.holiday.IHolidayDateActivity;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.dto.holiday.HolidayVo;
import com.linkallcloud.um.dto.holiday.WorkTimeVo;
import com.linkallcloud.um.server.dao.holiday.IHolidayDateDao;
import com.linkallcloud.um.server.dao.holiday.IWorkTimeDao;
import com.linkallcloud.um.server.utils.DateTimeUtils;
import com.linkallcloud.um.server.utils.DateUtil;
import com.linkallcloud.um.server.utils.HolidayUtil;

@Component
public class HolidayDateActivity extends BaseActivity<HolidayDate, IHolidayDateDao> implements IHolidayDateActivity {

	@Autowired
	private IHolidayDateDao holidayDateDao;

	@Autowired
	private IWorkTimeDao workTimeDao;

	@Override
	public IHolidayDateDao dao() {
		return holidayDateDao;
	}

	@Override
	public void initDefault(Sid company, int year) {
		initWeek(company, year);
	}

	private boolean initWeek(Sid company, int year) {
		Calendar start = Calendar.getInstance();
		start.set(year, 0, 1);
		List<HolidayDate> list = new ArrayList<>();
		while (start.get(Calendar.YEAR) == year && start.get(Calendar.MONTH) == 0) {
			HolidayDate date = new HolidayDate();
			date.setCompanyId(company.getId());
			date.setCompanyType(company.getCode());
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
	public void initWorkDay(Sid company, int year) {
		HolidayDate holidayDate = new HolidayDate();
		holidayDate.setCompanyId(company.getId());
		holidayDate.setCompanyType(company.getCode());
		String date = year + "-10" + "-01";
		holidayDate.setDate(date);
		HolidayDate result = holidayDateDao.queryHolidayDateLimit1(holidayDate);
		if (result == null) {
			initBaseWorkDay(company, year);
			initHoliday(company, year);
		} else if (result.getStatus() != 3) {
			// 时光荏苒，但是十月一日一定是法定节假日
			initHoliday(company, year);
		}
	}

	/**
	 * 生成周一到周五的工作日周末的非工作日
	 *
	 * @param year the year
	 */
	@Override
	public void initBaseWorkDay(Sid company, int year) {
		Calendar start = Calendar.getInstance();
		start.set(year, 0, 1);
		List<HolidayDate> list = new ArrayList<>();

		while (start.get(Calendar.YEAR) == year) {
			HolidayDate date = new HolidayDate();
			date.setCompanyId(company.getId());
			date.setCompanyType(company.getCode());
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
	public void initHoliday(Sid company, int year) {
		List<HolidayDate> allList = new ArrayList<>();
		for (int month = 1; month < 13; month++) {
			List<HolidayDate> monthList = initMonth(company, year, month);
			allList.addAll(monthList);
		}
		List<HolidayDate> monthList = initMonth(company, year - 1, 12);
		allList.addAll(monthList);
		holidayDateDao.batchInsertHolidayDate(allList);
	}

	@Override
	public HolidayDate getHoliday(Sid company, Integer day) {
		HolidayDate date = new HolidayDate();
		date.setCompanyId(company.getId());
		date.setCompanyType(company.getCode());
		date.setDate(DateUtil.formatDay(day));
		return holidayDateDao.queryHolidayDateLimit1(date);
	}

	@Override
	public List<HolidayDate> getHolidays(Sid company, String date) {
		HolidayDate record = new HolidayDate();
		record.setCompanyId(company.getId());
		record.setCompanyType(company.getCode());
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
	public List<HolidayDate> initMonth(Sid company, int year, int month) {
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
					holidayDate.setCompanyId(company.getId());
					holidayDate.setCompanyType(company.getCode());
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

	@Override
	public HolidayVo getCompanyHoliday(Trace t, Sid company, String date) {
		HolidayVo result = new HolidayVo();

		HolidayDate record = new HolidayDate();
		record.setCompanyId(company.getId());
		record.setCompanyType(company.getCode());
		String[] split = date.split("-");
		if (split.length == 1) {
			record.setYear(Integer.parseInt(split[0]));
		} else if (split.length == 2) {
			record.setYear(Integer.parseInt(split[0]));
			record.setMonth(Integer.parseInt(split[1]));
		} else if (split.length == 3) {
			record.setDate(date);
		}
		List<HolidayDate> dates = holidayDateDao.queryHolidayDate13(record);
		WorkTime worktime = workTimeDao.fetchByCompanyId(t, company.getId(), company.getCode());
		if (worktime != null) {
			result.setWorktime(new WorkTimeVo(worktime.getAmGoToWorkHour(), worktime.getAmGoToWorkMinute(),
					worktime.getAmGoOffWorkHour(), worktime.getAmGoOffWorkMinute(), worktime.getPmGoToWorkHour(),
					worktime.getPmGoToWorkMinute(), worktime.getPmGoOffWorkHour(), worktime.getPmGoOffWorkMinute()));
		}

		if (dates != null && !dates.isEmpty()) {
			for (HolidayDate hd : dates) {
				result.addHolidayDate(hd.getDate());
			}
		}

		return result;
	}

}
