package com.linkallcloud.um.server.service.ptj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.um.activity.ptj.IYwPartTimeJobActivity;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.ptj.YwPartTimeJob;
import com.linkallcloud.um.service.ptj.IYwPartTimeJobService;

@Service
@Transactional(readOnly = true)
public class YwPartTimeJobService extends PartTimeJobService<YwPartTimeJob, YwUser, IYwPartTimeJobActivity>
		implements IYwPartTimeJobService {

	@Autowired
	private IYwPartTimeJobActivity ywPartTimeJobActivity;

	@Override
	public IYwPartTimeJobActivity activity() {
		return ywPartTimeJobActivity;
	}

}
