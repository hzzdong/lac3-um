package com.linkallcloud.um.server.manager.holiday;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
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
	public void initDefault(Long companyId, int year) {
		service().initDefault(companyId, year);
	}

	@Override
	public void initWorkDay(Long companyId, int year) {
		service().initWorkDay(companyId, year);
	}

	@Override
	public void initBaseWorkDay(Long companyId, int year) {
		service().initBaseWorkDay(companyId, year);
	}

	@Override
	public void initHoliday(Long companyId, int year) {
		service().initHoliday(companyId, year);
	}

	@Override
	public HolidayDate getHoliday(Long companyId, Integer day) {
		return service().getHoliday(companyId, day);
	}

	@Override
	public List<HolidayDate> getHolidays(Long companyId, String date) {
		return service().getHolidays(companyId, date);
	}

	@Override
	public Boolean setHoliday(HolidayDate hd) {
		return service().setHoliday(hd);
	}

}
