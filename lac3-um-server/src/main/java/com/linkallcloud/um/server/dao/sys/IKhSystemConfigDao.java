package com.linkallcloud.um.server.dao.sys;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.KhSystemConfig;

public interface IKhSystemConfigDao extends IDao<KhSystemConfig> {

	KhSystemConfig fetch(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("configItemCode") String configItemCode);

}
