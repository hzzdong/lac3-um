package com.linkallcloud.um.pc.face;

import java.util.Calendar;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.response.ErrorFaceResponse;
import com.linkallcloud.um.domain.holiday.WorkTime;
import com.linkallcloud.um.face.CalendarRequest;
import com.linkallcloud.um.iapi.holiday.IHolidayDateManager;
import com.linkallcloud.um.iapi.holiday.IWorkTimeManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/WorkTime", method = RequestMethod.POST)
@Module(name = "工作时间设置")
public class WorkTimeFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IWorkTimeManager workTimeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IHolidayDateManager holidayDateManager;

	@Face(simple = true)
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public @ResponseBody Object get(CalendarRequest fr, Trace t, SessionUser su) {
		WorkTime entity = workTimeManager.fetchByCompanyId(t, su.companyId());
		if (entity == null) {
			return new ErrorFaceResponse("0", "no");
		}
		return entity;
	}

	@Face(simple = true)
	@RequestMapping(value = "/initCompanyWorkTime", method = RequestMethod.POST)
	public @ResponseBody Object initCompanyWorkTime(CalendarRequest fr, Trace t, SessionUser su) {
		Boolean ret = workTimeManager.initCompanyWorkTime(t, su.companyId());
		if (ret) {
			Calendar calendar = Calendar.getInstance();
			holidayDateManager.initWorkDay(su.companyId(), calendar.get(Calendar.YEAR));
		}
		return ret;
	}
	
	@Face(simple = true)
	@RequestMapping(value = "/setCompanyWorkTime", method = RequestMethod.POST)
	public @ResponseBody Object setCompanyWorkTime(ObjectFaceRequest<WorkTime> fr, Trace t, SessionUser su) {
		WorkTime entity = fr.getData();
		entity.setCompanyId(su.companyId());
		return workTimeManager.update(t, entity);
	}

}
