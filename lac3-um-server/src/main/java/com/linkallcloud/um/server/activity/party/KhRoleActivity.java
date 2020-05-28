package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.party.IKhRoleActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhRoleDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KhRoleActivity extends RoleActivity<KhRole, KhUser, KhCompany, IKhRoleDao, IKhUserDao, IKhCompanyDao> implements IKhRoleActivity {

    @Autowired
    private IKhUserDao khUserDao;

    @Autowired
    private IKhCompanyDao khCompanyDao;

    @Autowired
    private IKhRoleDao khRoleDao;

    @Autowired
    private IApplicationDao applicationDao;

    public KhRoleActivity() {
        super();
    }

    @Override
    public IKhUserDao userDao() {
        return khUserDao;
    }

    @Override
    public IKhCompanyDao orgDao() {
        return khCompanyDao;
    }

    @Override
    public IApplicationDao applicationDao() {
        return applicationDao;
    }

    @Override
    public IKhRoleDao dao() {
        return khRoleDao;
    }

    @Override
    public List<Menu> findPermedMenus(Trace t, Long roleId, Long appId) {
        return dao().findRoleAppMenus(t, roleId, appId);
    }
}
