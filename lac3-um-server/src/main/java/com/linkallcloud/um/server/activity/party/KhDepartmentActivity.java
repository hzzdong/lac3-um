package com.linkallcloud.um.server.activity.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.party.IKhDepartmentActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhDepartmentDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;

@Component
public class KhDepartmentActivity
		extends DepartmentActivity<KhDepartment, IKhDepartmentDao, KhUser, IKhUserDao, KhCompany, IKhCompanyDao>
		implements IKhDepartmentActivity {

	@Autowired
	private IKhDepartmentDao khDepartmentDao;

	@Autowired
	private IKhUserDao khUserDao;

	@Autowired
	private IKhCompanyDao khCompanyDao;

	public KhDepartmentActivity() {
		super();
	}

	@Override
	protected IKhCompanyDao getCompanyDao() {
		return khCompanyDao;
	}

	@Override
	protected IKhUserDao getUserDao() {
		return khUserDao;
	}

	@Override
	public IKhDepartmentDao dao() {
		return khDepartmentDao;
	}

	@Override
	protected void dealFullName(Trace t, boolean isNew, KhDepartment entity) {
		if (entity.getParentId() != null && entity.getParentClass().equals(KhDepartment.class.getSimpleName())) {
			KhDepartment parent = khDepartmentDao.fetchById(t, entity.getParentId());
			if (parent != null) {
				entity.setFullName(parent.getFullName() + "," + entity.getName());
			}
		} else {
			entity.setParentClass(null);
			entity.setParentId(0L);
			KhCompany parent = khCompanyDao.fetchById(t, entity.getCompanyId());
			if (parent != null) {
				entity.setFullName(parent.getName() + "," + entity.getName());
			}
		}
	}
}
