package com.linkallcloud.um.web.oapi.face.holiday;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.dubbo.config.annotation.DubboReference;
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

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IHolidayDateManager holidayDateManager;

	@Face(login = false)
	@RequestMapping(value = "/getHolidayDates", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(HolidayRequest faceReq, Trace t) throws Exception {
		if (faceReq.getCompanyId() == null || Strings.isBlank(faceReq.getCompanyType())) {
			return null;
		}

		if (Strings.isBlank(faceReq.getDate())) {
			faceReq.setDate(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));
		}

		return holidayDateManager.getCompanyHoliday(t, faceReq.getCompany(), faceReq.getDate());
	}

}
