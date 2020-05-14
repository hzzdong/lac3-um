package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.sys.IYwSystemConfigActivity;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.server.dao.sys.IYwSystemConfigDao;

@Component
public class YwSystemConfigActivity extends BaseActivity<YwSystemConfig, IYwSystemConfigDao>
		implements IYwSystemConfigActivity {

	@Autowired
	private IYwSystemConfigDao ywSystemConfigDao;

	@Override
	public IYwSystemConfigDao dao() {
		return ywSystemConfigDao;
	}

	@Override
	public YwSystemConfig fetch(Trace t, Long companyId, String configItemCode) {
		return dao().fetch(t, companyId, configItemCode);
	}
}
