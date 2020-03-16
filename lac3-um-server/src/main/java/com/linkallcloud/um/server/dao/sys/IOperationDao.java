package com.linkallcloud.um.server.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Operation;

public interface IOperationDao extends IDao<Operation> {

	List<Operation> findByMenuId(@Param("t") Trace t, @Param("menuId") Long menuId);

	List<Operation> findByAppId(@Param("t") Trace t, @Param("appId") Long appId);

}
