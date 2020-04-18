package com.linkallcloud.um.server.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.party.IKhRoleActivity;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.service.party.IKhRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhRoleService extends RoleService<KhRole, KhUser, KhCompany, IKhRoleActivity, IKhUserActivity, IKhCompanyActivity>
        implements IKhRoleService {

    @Autowired
    private IKhUserActivity khUserActivity;

    @Autowired
    private IKhCompanyActivity khCompanyActivity;

    @Autowired
    private IKhRoleActivity khRoleActivity;

    @Autowired
    private IApplicationActivity applicationActivity;

    @Override
    public IKhUserActivity userActivity() {
        return khUserActivity;
    }

    @Override
    public IKhCompanyActivity comapnyActivity() {
        return khCompanyActivity;
    }

    @Override
    public IApplicationActivity applicationActivity() {
        return applicationActivity;
    }

    @Override
    public IKhRoleActivity activity() {
        return khRoleActivity;
    }

    @Override
    public List<Menu> findPermedMenus(Trace t, Long roleId, Long appId) {
        return activity().findPermedMenus(t, roleId, appId);
    }

}
