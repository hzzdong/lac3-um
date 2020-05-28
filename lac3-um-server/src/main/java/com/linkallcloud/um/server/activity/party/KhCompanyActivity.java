package com.linkallcloud.um.server.activity.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhDepartmentDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IKhSystemConfigDao;
import com.linkallcloud.um.server.dao.sys.IMenuDao;

@Component
public class KhCompanyActivity
		extends CompanyActivity<KhCompany, IKhCompanyDao, KhUser, IKhUserDao, KhDepartment, IKhDepartmentDao>
		implements IKhCompanyActivity {

	@Autowired
	private IKhCompanyDao khCompanyDao;

	@Autowired
	private IKhUserDao khUserDao;

	@Autowired
	private IKhDepartmentDao khDepartmentDao;

	@Autowired
	private IKhSystemConfigDao khSystemConfigDao;

	@Autowired
	private IApplicationDao applicationDao;

	@Autowired
	private IMenuDao menuDao;

	@Override
	protected IKhUserDao getUserDao() {
		return khUserDao;
	}

	@Override
	public IKhCompanyDao dao() {
		return khCompanyDao;
	}

	@Override
	protected IKhDepartmentDao getDepartmentDao() {
		return khDepartmentDao;
	}

	@Override
	protected void autoAddSysAdminRole(Trace t, KhUser user) {
//        KhRole r = khRoleDao.fetchByGovCode(t, KhRole.class.getSimpleName() + Domains.SYS_ADMIN_ROLE_STUFF);
//        if (r != null) {
//            khRoleDao.addRoleUsers(t, r.getId(), new ArrayList<Long>(Arrays.asList(user.getId())));
//        }
	}

	public List<KhCompany> countByArea4id(Trace t, KhCompany entity) {
		return khCompanyDao.countByArea4id(t, entity);
	}

	@Override
	public Tree loadCompanyMenuTree(Trace t, Long companyId, Long appId) {
		Application app = applicationDao.fetchById(t, appId);
		if (app == null) {
			throw new ArgException("80000001", "无法查询对应的应用，可能是您的参数有误。");
		}

		Tree root = app.toMenuRoot();
		List<Menu> menus = menuDao.findKhCompanyAppMenusWithButton(t, companyId, appId, true);
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
		KhSystemConfig config = khSystemConfigDao.fetch(t, companyId, Consts.CONFIG_AREAS);
		if (config != null && !Strings.isBlank(config.getValue())) {
			List<NameValue> areas = config.parse();
			return areas;
		}
		return null;
	}

}
