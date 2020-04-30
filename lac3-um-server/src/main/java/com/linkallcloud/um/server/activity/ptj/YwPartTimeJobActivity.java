package com.linkallcloud.um.server.activity.ptj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.ptj.IYwPartTimeJobActivity;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.ptj.YwPartTimeJob;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.ptj.IYwPartTimeJobDao;

@Component
public class YwPartTimeJobActivity extends PartTimeJobActivity<YwPartTimeJob, YwUser, IYwPartTimeJobDao>
		implements IYwPartTimeJobActivity {

	@Autowired
	private IYwPartTimeJobDao ywPartTimeJobDao;

	@Autowired
	private IYwUserDao ywUserDao;

	@Autowired
	private IYwCompanyDao ywCompanyDao;

	@Autowired
	private IYwDepartmentDao ywDepartmentDao;

	@Override
	protected YwUser fetchUser(Trace t, Long userId, String userUuid) {
		return ywUserDao.fetchByIdUuid(t, userId, userUuid);
	}

	@Override
	protected Org fetchOrg(Trace t, Long orgId, String orgClass) {
		if (YwCompany.class.getSimpleName().equals(orgClass)) {
			return ywCompanyDao.fetchById(t, orgId);
		} else if (YwDepartment.class.getSimpleName().equals(orgClass)) {
			return ywDepartmentDao.fetchById(t, orgId);
		}
		return null;
	}

	@Override
	public IYwPartTimeJobDao dao() {
		return ywPartTimeJobDao;
	}
}
