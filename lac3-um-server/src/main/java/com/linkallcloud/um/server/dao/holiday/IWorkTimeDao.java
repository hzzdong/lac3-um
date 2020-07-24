package com.linkallcloud.um.server.dao.holiday;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.holiday.WorkTime;

public interface IWorkTimeDao extends IDao<WorkTime> {

	WorkTime fetchByCompanyId(@Param("t") Trace t, @Param("companyId") Long companyId);

}
