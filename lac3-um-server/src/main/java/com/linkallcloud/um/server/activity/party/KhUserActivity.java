package com.linkallcloud.um.server.activity.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhDepartmentDao;
import com.linkallcloud.um.server.dao.party.IKhRoleDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;

@Component
public class KhUserActivity extends
		UserActivity<KhUser, IKhUserDao, KhDepartment, IKhDepartmentDao, KhCompany, IKhCompanyDao, KhRole, IKhRoleDao>
		implements IKhUserActivity {

	@Autowired
	private IKhUserDao khUserDao;

	@Autowired
	private IKhDepartmentDao khDepartmentDao;

	@Autowired
	private IKhCompanyDao khCompanyDao;

	@Autowired
	private IKhRoleDao khRoleDao;

	public KhUserActivity() {
		super();
	}

	@Override
	public IKhUserDao dao() {
		return khUserDao;
	}

	@Override
	protected IKhDepartmentDao getDepartmentDao() {
		return khDepartmentDao;
	}

	@Override
	protected IKhCompanyDao getCompanyDao() {
		return khCompanyDao;
	}

	@Override
	protected IKhRoleDao getRoleDao() {
		return khRoleDao;
	}

	@Override
	protected String departmentAdminRoleCode() {
		return "KhRole_sys_dept";
	}

}
