package com.linkallcloud.um.server.activity.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.LacLogActivity;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.um.activity.sys.IUmLogActivity;
import com.linkallcloud.um.server.dao.sys.IUmLogDao;

@Component
public class UmLogActivity extends LacLogActivity<BusiLog, IUmLogDao> implements IUmLogActivity {

	@Autowired
	private IUmLogDao logDao;

	@Override
	public IUmLogDao dao() {
		return logDao;
	}
}
