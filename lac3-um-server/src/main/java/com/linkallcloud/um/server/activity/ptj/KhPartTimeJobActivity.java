package com.linkallcloud.um.server.activity.ptj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.ptj.IKhPartTimeJobActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.ptj.KhPartTimeJob;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhDepartmentDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;
import com.linkallcloud.um.server.dao.ptj.IKhPartTimeJobDao;

@Component
public class KhPartTimeJobActivity extends PartTimeJobActivity<KhPartTimeJob, KhUser, IKhPartTimeJobDao>
		implements IKhPartTimeJobActivity {

	@Autowired
	private IKhPartTimeJobDao khPartTimeJobDao;

	@Autowired
	private IKhUserDao khUserDao;

	@Autowired
	private IKhCompanyDao khCompanyDao;

	@Autowired
	private IKhDepartmentDao khDepartmentDao;

	@Override
	protected KhUser fetchUser(Trace t, Long userId, String userUuid) {
		return khUserDao.fetchByIdUuid(t, userId, userUuid);
	}

	@Override
	protected Org fetchOrg(Trace t, Long orgId, String orgClass) {
		if (KhCompany.class.getSimpleName().equals(orgClass)) {
			return khCompanyDao.fetchById(t, orgId);
		} else if (KhDepartment.class.getSimpleName().equals(orgClass)) {
			return khDepartmentDao.fetchById(t, orgId);
		}
		return null;
	}

	@Override
	public IKhPartTimeJobDao dao() {
		return khPartTimeJobDao;
	}

}
