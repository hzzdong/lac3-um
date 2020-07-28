package com.linkallcloud.um.web.oapi.face.holiday;

import java.util.Calendar;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.dto.holiday.HolidayRequest;
import com.linkallcloud.um.iapi.holiday.IHolidayDateManager;
import com.linkallcloud.web.face.annotation.Face;

@Controller
@RequestMapping(value = "/face/Holiday", method = RequestMethod.POST)
@Module(name = "假日")
public class HolidayFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IHolidayDateManager holidayDateManager;

	@Face(login = false)
	@RequestMapping(value = "/getHolidayDates", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(HolidayRequest faceReq, Trace t) throws Exception {
		if (faceReq.getCompanyId() == null || Strings.isBlank(faceReq.getCompanyType())) {
			return null;
		}
		
		if (Strings.isBlank(faceReq.getDate())) {
			Calendar start = Calendar.getInstance();
			faceReq.setDate(start.get(Calendar.YEAR) + "");
		}
		
		return holidayDateManager.getCompanyHoliday(t, faceReq.getCompany(), faceReq.getDate());
	}

}
