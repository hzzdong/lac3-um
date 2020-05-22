package com.linkallcloud.um.server.activity.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.exception.AccountException;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;
import com.linkallcloud.um.server.dao.sys.IYwSystemConfigDao;

@Component
public class YwCompanyActivity
		extends CompanyActivity<YwCompany, IYwCompanyDao, YwUser, IYwUserDao, YwDepartment, IYwDepartmentDao>
		implements IYwCompanyActivity {

	@Autowired
	private IYwCompanyDao ywwCompanyDao;

	@Autowired
	private IYwUserDao ywUserDao;

	@Autowired
	private IYwDepartmentDao ywDepartmentDao;

	@Autowired
	private IApplicationDao applicationDao;

	@Autowired
	private IMenuDao menuDao;

	@Autowired
	private IYwSystemConfigDao ywSystemConfigDao;

	public YwCompanyActivity() {
		super();
	}

	@Override
	protected IYwUserDao getUserDao() {
		return ywUserDao;
	}

	@Override
	public IYwCompanyDao dao() {
		return ywwCompanyDao;
	}

	@Override
	protected IYwDepartmentDao getDepartmentDao() {
		return ywDepartmentDao;
	}

	@Override
	public boolean checkUserExist(Trace t, boolean isNew, YwCompany entity) {
		if (isNew) {
			if (!Strings.isBlank(entity.getGovCode())) {
				YwUser dbUser = getUserDao().fecthByAccount(t, entity.getGovCode());
				if (dbUser != null) {
					throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "账号已经存在：" + entity.getGovCode());
				}

				Account account = accountDao.fecthByAccount(t, entity.getGovCode());
				if (account != null) {
					throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "账号已经存在：" + entity.getGovCode());
				}
			}
		}
		return false;
	}

	@Override
	protected void autoCreateAdmin(Trace t, YwCompany entity) {
		YwUser user = new YwUser(entity.getName() + "_管理员", entity.getGovCode(), entity.getPhone(),
				entity.getGovCode().length() >= 6 ? entity.getGovCode()
						: (entity.getGovCode() + Strings.dup('0', 6 - entity.getGovCode().length())));
		user.setSalt(user.generateUuid());
		user.setPassword(Securities.password4Src(user.getPassword(), user.getSalt()));
		user.setParent(entity);
		user.setCompanyId(entity.getId());
		user.setType(Domains.USER_ADMIN);
		getUserDao().insert(t, user);

		user.setCode(Domains.genDomainCode(entity, user));
		getUserDao().updateCode(t, user.getId(), user.getCode());

		autoCreateAccount(t, user);
		autoAddSysAdminRole(t, user);
	}

	@Override
	protected void autoAddSysAdminRole(Trace t, YwUser user) {
//        YwRole r = ywRoleDao.fetchByGovCode(t, YwRole.class.getSimpleName() + Domains.SYS_ADMIN_ROLE_STUFF);
//        if (r != null) {
//            ywRoleDao.addRoleUsers(t, r.getId(), new ArrayList<Long>(Arrays.asList(user.getId())));
//        }
	}

	@Override
	public Tree loadCompanyMenuTree(Trace t, Long companyId, Long appId) {
		Application app = applicationDao.fetchById(t, appId);
		if (app == null) {
			throw new ArgException("80000001", "无法查询对应的应用，可能是您的参数有误。");
		}

		Tree root = app.toMenuRoot();
		YwCompany company = dao().fetchById(t, companyId);

		List<Menu> menus = null;
		if (company.isTopParent()) {
			menus = menuDao.findYwCompanyAppMenus(t, null, appId, true);
		} else {
			menus = menuDao.findYwCompanyAppMenus(t, companyId, appId, true);
		}

		Trees.assembleDomain2Tree(root, menus);
		root.sort();
		return root;
	}

	@Override
	public Long[] getConfigCompanyAreaRootIds(Trace t, Long companyId) {
		List<NameValue> areas = getConfigCompanyAreaRoots(t, companyId);
		if (areas != null && areas.size() > 0) {
			Long[] areaRootIds = new Long[areas.size()];
			for (int i = 0; i < areas.size(); i++) {
				areaRootIds[i] = Long.parseLong(areas.get(i).getKey());
			}
			return areaRootIds;
		}
		return null;
	}

	@Override
	public List<NameValue> getConfigCompanyAreaRoots(Trace t, Long companyId) {
		YwSystemConfig config = ywSystemConfigDao.fetch(t, companyId, Consts.CONFIG_AREAS);
		if (config != null && !Strings.isBlank(config.getValue())) {
			List<NameValue> areas = config.parse();
			return areas;
		}
		return null;
	}

}
