package com.linkallcloud.um.server.activity.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.domain.sys.YwAccount;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwRoleDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;
import com.linkallcloud.um.server.dao.sys.IYwAccountDao;

@Component
public class YwUserActivity extends
		UserActivity<YwUser, IYwUserDao, YwDepartment, IYwDepartmentDao, YwCompany, IYwCompanyDao, YwRole, IYwRoleDao, YwAccount, IYwAccountDao>
		implements IYwUserActivity {

	@Autowired
	private IYwUserDao ywUserDao;

	@Autowired
	private IYwDepartmentDao ywDepartmentDao;

	@Autowired
	private IYwCompanyDao ywCompanyDao;

	@Autowired
	private IYwRoleDao ywRoleDao;

	@Autowired
	private IYwAccountDao ywAccountDao;

	@Autowired
	protected IMenuDao menuDao;

	public YwUserActivity() {
		super();
	}

	@Override
	public IYwUserDao dao() {
		return ywUserDao;
	}

	@Override
	protected IYwAccountDao getAccountDao() {
		return ywAccountDao;
	}

	@Override
	protected IYwDepartmentDao getDepartmentDao() {
		return ywDepartmentDao;
	}

	@Override
	protected IYwCompanyDao getCompanyDao() {
		return ywCompanyDao;
	}

	@Override
	protected IYwRoleDao getRoleDao() {
		return ywRoleDao;
	}

	@Transactional(readOnly = false)
	@Override
	public void cleanOtherUserMobileByUserId(Trace t, String mobile, Long userId) {
		Query query = new Query();
		query.addRule(new Equal("mobileEq", mobile));
		List<YwUser> users = find(t, query);
		if (users != null && users.size() > 0) {
			for (YwUser user : users) {
				if (!user.getId().equals(userId)) {
					user.setMobile("");
					user.setPassword(null);
					user.setSalt(null);
					dao().update(t, user);
				}
			}
		}
	}

	@Override
	public YwUser findByMobileAndDdStatus(Trace t, String mobile) {
		return dao().findByMobileAndDdStatus(t, mobile);
	}

	@Override
	protected String departmentAdminRoleCode() {
		return "YwRole_sys_dept";
	}

	@Override
	protected List<Menu> findCompanyAppMenusWithButton(Trace t, Long companyId, Long appId) {
		YwCompany company = getCompanyDao().fetchById(t, companyId);
		if (company.isTopParent()) {
			return menuDao.findAppMenusWithButton(t, appId, true);
		} else {
			return menuDao.findYwCompanyAppMenusWithButton(t, companyId, appId, true);
		}
	}

	@Override
	protected void autoCreateAccount(Trace t, YwUser entity) {
		YwAccount account = new YwAccount(entity.getName(), entity.getMobile(), entity.getAccount(),
				entity.getPassword(), entity.getSalt());
		getAccountDao().insert(t, account);
	}
	
	@Override
	protected int updateUserAccountStatus(Trace t, int status, Long userId, String userUuid) {
		YwUser user = fetchByIdUuid(t, userId, userUuid);
		YwAccount account = getAccountDao().fecthByAccount(t, user.getAccount());
		return getAccountDao().updateStatus(t, status, account.getId(), account.getUuid());
	}

	@Override
	protected int updateUserAccountStatusByCompany(Trace t, int status, Long companyId) {
		return getAccountDao().updateStatusByCompany(t, status, companyId);
	}

	@Override
	protected int updateUserAccountStatusByDepartment(Trace t, int status, Long departmentId) {
		return getAccountDao().updateStatusByDepartment(t, status, departmentId);
	}

}
