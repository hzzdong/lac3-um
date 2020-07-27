package com.linkallcloud.um.server.manager.holiday;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.holiday.HolidayDate;
import com.linkallcloud.um.iapi.holiday.IHolidayDateManager;
import com.linkallcloud.um.service.holiday.IHolidayDateService;

@Service(interfaceClass = IHolidayDateManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "假日")
public class HolidayDateManager extends BaseManager<HolidayDate, IHolidayDateService> implements IHolidayDateManager {

    @Autowired
    private IHolidayDateService holidayDateService;

    @Override
    protected IHolidayDateService service() {
        return holidayDateService;
    }

	@Override
	public void initDefault(Sid company, int year) {
		service().initDefault(company, year);
	}

	@Override
	public void initWorkDay(Sid company, int year) {
		service().initWorkDay(company, year);
	}

	@Override
	public void initBaseWorkDay(Sid company, int year) {
		service().initBaseWorkDay(company, year);
	}

	@Override
	public void initHoliday(Sid company, int year) {
		service().initHoliday(company, year);
	}

	@Override
	public HolidayDate getHoliday(Sid company, Integer day) {
		return service().getHoliday(company, day);
	}

	@Override
	public List<HolidayDate> getHolidays(Sid company, String date) {
		return service().getHolidays(company, date);
	}

	@Override
	public Boolean setHoliday(HolidayDate hd) {
		return service().setHoliday(hd);
	}

}
