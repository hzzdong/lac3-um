package com.linkallcloud.um.server.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.sys.Application;

public interface IApplicationDao extends IDao<Application> {

	Application fetchByCode(@Param("t") Trace t, @Param("code") String code);

	List<Application> findPage4YwRole(@Param("t") Trace t, @Param("page") Page<Application> page);

	List<Application> findPage4KhRole(@Param("t") Trace t, @Param("page") Page<Application> page);

	List<Application> findPage4KhCompany(@Param("t") Trace t, @Param("page") Page<Application> page);

	List<Application> find4YwUser(@Param("t") Trace t, @Param("ywUserId") Long ywUserId);

	List<Application> find4KhUser(@Param("t") Trace t, @Param("khUserId") Long khUserId);

	int updateInterfaceInfo(@Param("t") Trace t, @Param("entity") Application entity);

    int updateMappingInfo(@Param("t") Trace t, @Param("entity")Application entity);
}
