package com.linkallcloud.um.pc.aop;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.iapi.sys.IOperationManager;
import com.linkallcloud.web.interceptors.AbstractPermissionInterceptor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LacPermissionInterceptor extends AbstractPermissionInterceptor {

    private static Map<Long, Map<String, String[]>> appUriRescodeMap = new HashMap<>();

    @DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IOperationManager operationManager;

    @Value("${oapi.appcode}")
    protected String myAppCode;

    public LacPermissionInterceptor() {
        super();
    }

    public LacPermissionInterceptor(List<String> ignoreRes, boolean override) {
        super(ignoreRes, override);
    }

    @Override
    protected String getAppCode() {
        return myAppCode;
    }

    @Override
    protected Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
        if (appUriRescodeMap.containsKey(appId)) {
            return appUriRescodeMap.get(appId);
        } else {
            Map<String, String[]> thisRes = operationManager.loadAppUriRescodeMap(t, appId);
            appUriRescodeMap.put(appId, thisRes);
            return thisRes;
        }
    }

}
