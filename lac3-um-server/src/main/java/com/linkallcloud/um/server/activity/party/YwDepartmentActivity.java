package com.linkallcloud.um.server.activity.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.activity.party.IYwDepartmentActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.sys.IAreaDao;

@Component
public class YwDepartmentActivity
		extends DepartmentActivity<YwDepartment, IYwDepartmentDao, YwUser, IYwUserDao, YwCompany, IYwCompanyDao>
		implements IYwDepartmentActivity {

	@Autowired
	private IYwDepartmentDao ywDepartmentDao;

	@Autowired
	private IYwUserDao ywUserDao;

	@Autowired
	private IYwCompanyDao ywCompanyDao;

	@Autowired
	private IAreaDao areaDao;

	@Override
	protected IYwUserDao getUserDao() {
		return ywUserDao;
	}

	@Override
	public IYwDepartmentDao dao() {
		return ywDepartmentDao;
	}

	@Override
	protected IYwCompanyDao getCompanyDao() {
		return ywCompanyDao;
	}

	@Override
	protected void dealFullName(Trace t, boolean isNew, YwDepartment entity) {
		if (entity != null && entity.getCompanyId() != null) {
			YwCompany company = getCompanyDao().fetchById(t, entity.getCompanyId());
			if (company != null) {
				if (company.getAreaId() != null) {
					Area area = areaDao.fetchById(t, company.getAreaId());
					if (area != null) {
						entity.setFullName(area.getFullName() + entity.getName());
					}
				} else if (!Strings.isBlank(company.getGovCode())) {
					Area area = areaDao.fetchByGovCode(t, company.getGovCode());
					if (area != null) {
						entity.setFullName(area.getFullName() + entity.getName());
					}
				}
			}
		}
	}

}
