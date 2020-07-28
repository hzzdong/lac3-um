package com.linkallcloud.um.kh.face;

import org.apache.dubbo.config.annotation.DubboReference;
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
import com.linkallcloud.um.iapi.holiday.IWorkTimeManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/WorkTime", method = RequestMethod.POST)
@Module(name = "工作时间设置")
public class WorkTimeFace {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IWorkTimeManager workTimeManager;

	@Face(simple = true)
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public @ResponseBody Object get(CalendarRequest fr, Trace t, SessionUser su) {
		WorkTime entity = workTimeManager.fetchByCompanyId(t, su.getCompany());
		if (entity == null) {
			return new ErrorFaceResponse("0", "no");
		}
		return entity;
	}

	@Face(simple = true)
	@RequestMapping(value = "/initCompanyWorkTime", method = RequestMethod.POST)
	public @ResponseBody Object initCompanyWorkTime(CalendarRequest fr, Trace t, SessionUser su) {
		return workTimeManager.initCompanyHolidayWorkTime(t, su.getCompany());
	}

	@Face(simple = true)
	@RequestMapping(value = "/setCompanyWorkTime", method = RequestMethod.POST)
	public @ResponseBody Object setCompanyWorkTime(ObjectFaceRequest<WorkTime> fr, Trace t, SessionUser su) {
		WorkTime entity = fr.getData();
		entity.setCompanyId(su.getCompany().getId());
		entity.setCompanyType(su.getCompany().getCode());
		return workTimeManager.update(t, entity);
	}

}
