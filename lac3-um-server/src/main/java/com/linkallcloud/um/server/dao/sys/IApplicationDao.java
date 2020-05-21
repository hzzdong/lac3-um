package com.linkallcloud.um.server.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;

public interface IApplicationDao extends IDao<Application> {

	Application fetchByCode(@Param("t") Trace t, @Param("code") String code);

	List<Application> findPage4YwRole(@Param("t") Trace t, @Param("page") Page<Application> page);
	List<Application> findPage4YwRole4Select(@Param("t") Trace t, @Param("page") Page<Application> page);

	List<Application> findPage4KhRole(@Param("t") Trace t, @Param("page") Page<Application> page);
	List<Application> findPage4KhRole4Select(@Param("t") Trace t, @Param("page") Page<Application> page);

	List<Application> findPage4KhCompany(@Param("t") Trace t, @Param("page") Page<Application> page);
	List<Application> findPage4SelfKhCompany4Select(@Param("t") Trace t, @Param("page") Page<Application> page);
	
	List<Application> findPage4SelfYwCompany(@Param("t") Trace t, @Param("page") Page<Application> page);
	List<Application> findPage4SelfYwCompany4Select(@Param("t") Trace t, @Param("page") Page<Application> page);

	List<Application> find4YwUser(@Param("t") Trace t, @Param("ywUserId") Long ywUserId);

	List<Application> find4KhUser(@Param("t") Trace t, @Param("khUserId") Long khUserId);

	int updateInterfaceInfo(@Param("t") Trace t, @Param("entity") Application entity);

	int updateMappingInfo(@Param("t") Trace t, @Param("entity") Application entity);

	int updateIco(@Param("t") Trace t, @Param("app") Sid app, @Param("ico") String ico);
}
