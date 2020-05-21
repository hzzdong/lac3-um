package com.linkallcloud.um.server.service.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.party.IKhDepartmentActivity;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.service.party.IKhCompanyService;

@Service
@Transactional(readOnly = true)
public class KhCompanyService extends
		CompanyService<KhCompany, IKhCompanyActivity, KhUser, IKhUserActivity, KhDepartment, IKhDepartmentActivity>
		implements IKhCompanyService {

	@Autowired
	private IKhCompanyActivity khCompanyActivity;

	@Autowired
	private IKhUserActivity khUserActivity;

	@Autowired
	private IKhDepartmentActivity khDepartmentActivity;

	@Override
	protected IKhUserActivity getUserActivity() {
		return khUserActivity;
	}

	@Override
	public IKhCompanyActivity activity() {
		return khCompanyActivity;
	}

	@Override
	protected IKhDepartmentActivity getDepartmentActivity() {
		return khDepartmentActivity;
	}

	public List<KhCompany> countByArea4id(Trace t, KhCompany entity) {
		return activity().countByArea4id(t, entity);
	}

	@Override
	public Tree findCompanyValidMenuTree(Trace t, Long companyId, Long appId) {
		return activity().findCompanyValidMenuTree(t, companyId, appId);
	}

}
