package com.linkallcloud.um.server.service.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwDepartmentActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.service.party.IYwCompanyService;

@Service
@Transactional(readOnly = true)
public class YwCompanyService extends
		CompanyService<YwCompany, IYwCompanyActivity, YwUser, IYwUserActivity, YwDepartment, IYwDepartmentActivity>
		implements IYwCompanyService {

	@Autowired
	private IYwCompanyActivity ywwCompanyActivity;

	@Autowired
	private IYwUserActivity ywUserActivity;

	@Autowired
	private IYwDepartmentActivity ywDepartmentActivity;

	@Override
	protected IYwUserActivity getUserActivity() {
		return ywUserActivity;
	}

	@Override
	public IYwCompanyActivity activity() {
		return ywwCompanyActivity;
	}

	@Override
	protected IYwDepartmentActivity getDepartmentActivity() {
		return ywDepartmentActivity;
	}

}
