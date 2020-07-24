package com.linkallcloud.um.pc.face;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.face.CalendarRequest;
import com.linkallcloud.um.iapi.holiday.IHolidayDateManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/Holiday", method = RequestMethod.POST)
@Module(name = "假日设置")
public class HolidayFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IHolidayDateManager holidayDateManager;

	@Face(simple = true)
	@RequestMapping(value = "/getByMonth", method = RequestMethod.POST)
	public @ResponseBody Object getByMonth(CalendarRequest fr, Trace t, SessionUser su) {
		String date = null;
		if (fr.getYear() != null && fr.getMonth() != null) {
			date = fr.getYear() + "-" + (fr.getMonth() >= 10 ? fr.getMonth() : ("0" + fr.getMonth()));
		} else {
			date = new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime());
		}
		List<HolidayDate> entities = holidayDateManager.getHolidays(su.companyId(), date);
		return entities;
	}

	@Face(simple = true)
	@RequestMapping(value = "/setHoliday", method = RequestMethod.POST)
	public @ResponseBody Object setHoliday(CalendarRequest fr, Trace t, SessionUser su) {
		HolidayDate hd = new HolidayDate();
		hd.setCompanyId(su.companyId());
		hd.setDate(fr.getYear() + "-" + (fr.getMonth() >= 10 ? fr.getMonth() : ("0" + fr.getMonth())) + "-"
				+ (fr.getDay() >= 10 ? fr.getDay() : ("0" + fr.getDay())));
		hd.setStatus(fr.getStatus());
		return holidayDateManager.setHoliday(hd);
	}

}
