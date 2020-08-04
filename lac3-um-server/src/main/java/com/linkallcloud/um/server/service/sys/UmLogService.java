package com.linkallcloud.um.server.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.service.LacLogService;
import com.linkallcloud.um.activity.sys.IUmLogActivity;
import com.linkallcloud.um.service.sys.IUmLogService;

@Service
@Transactional(readOnly = true)
public class UmLogService extends LacLogService<BusiLog, IUmLogActivity> implements IUmLogService {

	@Autowired
	private IUmLogActivity umLogActivity;

	@Override
	public IUmLogActivity activity() {
		return umLogActivity;
	}

}
