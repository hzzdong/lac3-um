package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.party.IYwRoleActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwRoleDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YwRoleActivity extends RoleActivity<YwRole, YwUser, YwCompany, IYwRoleDao, IYwUserDao, IYwCompanyDao> implements IYwRoleActivity {

    @Autowired
    private IYwUserDao ywUserDao;

    @Autowired
    private IYwCompanyDao ywCompanyDao;

    @Autowired
    private IYwRoleDao ywRoleDao;

    @Autowired
    private IApplicationDao applicationDao;

    public YwRoleActivity() {
        super();
    }

    @Override
    public IYwUserDao userDao() {
        return ywUserDao;
    }

    @Override
    public IYwCompanyDao orgDao() {
        return ywCompanyDao;
    }

    @Override
    public IYwRoleDao dao() {
        return ywRoleDao;
    }

    @Override
    public IApplicationDao applicationDao() {
        return applicationDao;
    }

    @Override
    public List<Menu> findPermedMenus(Trace t, Long roleId, Long appId) {
        return dao().findPermedMenus(t, roleId, appId);
    }
}
