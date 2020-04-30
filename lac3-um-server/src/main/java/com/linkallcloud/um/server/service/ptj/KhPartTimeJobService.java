package com.linkallcloud.um.server.service.ptj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.um.activity.ptj.IKhPartTimeJobActivity;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.ptj.KhPartTimeJob;
import com.linkallcloud.um.service.ptj.IKhPartTimeJobService;

@Service
@Transactional(readOnly = true)
public class KhPartTimeJobService extends PartTimeJobService<KhPartTimeJob, KhUser, IKhPartTimeJobActivity>
		implements IKhPartTimeJobService {

	@Autowired
	private IKhPartTimeJobActivity khPartTimeJobActivity;

	@Override
	public IKhPartTimeJobActivity activity() {
		return khPartTimeJobActivity;
	}

}
