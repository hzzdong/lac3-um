package com.linkallcloud.um.server.service.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.party.IKhDepartmentActivity;
import com.linkallcloud.um.activity.party.IKhRoleActivity;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.service.party.IKhUserService;

@Service
@Transactional(readOnly = true)
public class KhUserService extends
		UserService<KhUser, IKhUserActivity, KhDepartment, IKhDepartmentActivity, KhCompany, IKhCompanyActivity, KhRole, IKhRoleActivity>
		implements IKhUserService {

	@Autowired
	private IKhUserActivity khUserActivity;

	@Autowired
	private IKhDepartmentActivity khDepartmentActivity;

	@Autowired
	private IKhCompanyActivity khCompanyActivity;

	@Autowired
	private IKhRoleActivity khRoleActivity;

//    @Autowired
//    private IYwCompanyActivity ywCompanyActivity;
//
//    @Autowired
//    private IYwUserActivity ywUserActivity;
//
//    @Autowired
//    private IAreaActivity areaActivity;

	@Override
	public IKhUserActivity activity() {
		return khUserActivity;
	}

	@Override
	protected IKhDepartmentActivity getDepartmentActivity() {
		return khDepartmentActivity;
	}

	@Override
	protected IKhCompanyActivity getCompanyActivity() {
		return khCompanyActivity;
	}

	@Override
	protected IKhRoleActivity getRoleActivity() {
		return khRoleActivity;
	}

}
