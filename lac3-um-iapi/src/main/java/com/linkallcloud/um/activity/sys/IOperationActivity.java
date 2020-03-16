package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Operation;

import java.util.List;
import java.util.Map;

public interface IOperationActivity extends IActivity<Operation> {

    List<Operation> findByMenuId(Trace t, Long menuId);

    List<Operation> findByAppId(Trace t, Long appId);

    Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId);

}
