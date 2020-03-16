package com.linkallcloud.um.server.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwRoleActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.service.party.IYwRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class YwRoleService extends RoleService<YwRole, YwUser, YwCompany, IYwRoleActivity, IYwUserActivity, IYwCompanyActivity>
        implements IYwRoleService {

    @Autowired
    private IYwUserActivity ywUserActivity;

    @Autowired
    private IYwCompanyActivity ywCompanyActivity;

    @Autowired
    private IYwRoleActivity ywRoleActivity;

    @Autowired
    private IApplicationActivity applicationActivity;

    @Override
    public IYwUserActivity userActivity() {
        return ywUserActivity;
    }

    @Override
    public IYwCompanyActivity orgActivity() {
        return ywCompanyActivity;
    }

    @Override
    public IYwRoleActivity activity() {
        return ywRoleActivity;
    }

    @Override
    public IApplicationActivity applicationActivity() {
        return applicationActivity;
    }

    @Override
    public List<Menu> findPermedMenus(Trace t, Long roleId, Long appId) {
        return activity().findPermedMenus(t, roleId, appId);
    }

}
