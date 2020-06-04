package com.linkallcloud.um.server.activity.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.KhAccount;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhDepartmentDao;
import com.linkallcloud.um.server.dao.party.IKhRoleDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;
import com.linkallcloud.um.server.dao.sys.IKhAccountDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;

@Component
public class KhUserActivity extends
		UserActivity<KhUser, IKhUserDao, KhDepartment, IKhDepartmentDao, KhCompany, IKhCompanyDao, KhRole, IKhRoleDao, KhAccount, IKhAccountDao>
		implements IKhUserActivity {

	@Autowired
	private IKhUserDao khUserDao;

	@Autowired
	private IKhDepartmentDao khDepartmentDao;

	@Autowired
	private IKhCompanyDao khCompanyDao;

	@Autowired
	private IKhRoleDao khRoleDao;

	@Autowired
	private IKhAccountDao khAccountDao;

	@Autowired
	protected IMenuDao menuDao;

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
	protected IKhAccountDao getAccountDao() {
		return khAccountDao;
	}

	@Override
	protected String departmentAdminRoleCode() {
		return "KhRole_sys_dept";
	}

	@Override
	protected List<Menu> findCompanyAppMenusWithButton(Trace t, Long companyId, Long appId) {
		return menuDao.findKhCompanyAppMenusWithButton(t, companyId, appId, true);
	}

	@Override
	public Page<KhUser> page4UnRole4Yw(Trace t, Page<KhUser> page) {
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
		String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());
		KhRole role = getRoleDao().fetchByIdUuid(t, roleId, roleUuid);
		if (role == null) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Equal r = (Equal) page.getRule4Field("type");
		if (r != null) {
			r.setValue(role.getType());
		} else {
			page.addRule(new Equal("type", role.getType()));
		}

		Equal rl = (Equal) page.getRule4Field("level");
		if (rl != null) {
			rl.setValue(role.getLevel());
		} else {
			page.addRule(new Equal("level", role.getLevel()));
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<KhUser> list = dao().page4UnRole4Yw(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<KhUser>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public Page<KhUser> page4Role4Yw(Trace t, Page<KhUser> page) {
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long roleId = Castors.me().castTo(((Equal) page.getRule4Field("roleId")).getValue(), Long.class);
		String roleUuid = Castors.me().castToString(((Equal) page.getRule4Field("roleUuid")).getValue());
		KhRole role = getRoleDao().fetchByIdUuid(t, roleId, roleUuid);
		if (role == null) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Equal r = (Equal) page.getRule4Field("type");
		if (r != null) {
			r.setValue(role.getType());
		} else {
			page.addRule(new Equal("type", role.getType()));
		}

		Equal rl = (Equal) page.getRule4Field("level");
		if (rl != null) {
			rl.setValue(role.getLevel());
		} else {
			page.addRule(new Equal("level", role.getLevel()));
		}

		page.checkPageParameters();
		try {
			PageHelper.startPage(page.getPageNum(), page.getLength());
			List<KhUser> list = dao().page4Role4Yw(t, page);
			if (list instanceof com.github.pagehelper.Page) {
				page.setRecordsTotal(((com.github.pagehelper.Page<KhUser>) list).getTotal());
				page.checkPageParameters();
				page.setRecordsFiltered(page.getRecordsTotal());
				page.addDataAll(list);
			}
			return page;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	protected void autoCreateAccount(Trace t, KhUser entity) {
		KhAccount account = new KhAccount(entity.getName(), entity.getMobile(), entity.getAccount(),
				entity.getPassword(), entity.getSalt());
		getAccountDao().insert(t, account);
	}

}
