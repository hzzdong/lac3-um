package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.sys.IOperationActivity;
import com.linkallcloud.um.domain.sys.Operation;
import com.linkallcloud.um.server.dao.sys.IOperationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OperationActivity extends BaseActivity<Operation, IOperationDao> implements IOperationActivity {

    @Autowired
    private IOperationDao operationDao;

    @Override
    public IOperationDao dao() {
        return operationDao;
    }

    @Override
    public List<Operation> findByMenuId(Trace t, Long menuId) {
        return dao().findByMenuId(t, menuId);
    }

    @Override
    public List<Operation> findByAppId(Trace t, Long appId) {
        return dao().findByAppId(t, appId);
    }

    @Override
    public Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
        List<Operation> list = findByAppId(t, appId);
        Map<String, Set<String>> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (Operation oper : list) {
                if (map.containsKey(oper.getUri())) {
                    map.get(oper.getUri()).add(oper.getMenuGovCode());
                } else {
                    Set<String> rescodes = new HashSet<String>();
                    rescodes.add(oper.getMenuGovCode());
                    map.put(oper.getUri(), rescodes);
                }
            }
        }

        Map<String, String[]> result = new HashMap<>();
        Iterator<String> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String uri = itr.next();
            Set<String> rescodes = map.get(uri);
            String[] rescodesArry = rescodes.toArray(new String[0]);
            result.put(uri, rescodesArry);
        }

        return result;
    }
}
