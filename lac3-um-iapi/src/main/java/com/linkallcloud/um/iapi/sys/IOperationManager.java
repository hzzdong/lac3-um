package com.linkallcloud.um.iapi.sys;

import java.util.List;
import java.util.Map;

import com.linkallcloud.dto.Trace;
import com.linkallcloud.manager.IManager;
import com.linkallcloud.um.domain.sys.Operation;

public interface IOperationManager extends IManager<Long, Operation> {
	
	List<Operation> findByMenuId(Trace t, Long menuId);
	List<Operation> findByAppId(Trace t, Long appId);
	
    Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId);

}
