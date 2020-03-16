package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.sys.Operation;

import java.util.List;
import java.util.Map;

public interface IOperationService extends IService<Operation> {

	List<Operation> findByMenuId(Trace t, Long menuId);

	List<Operation> findByAppId(Trace t, Long appId);

	Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId);

}
