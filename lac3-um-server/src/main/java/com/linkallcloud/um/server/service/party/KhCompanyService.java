package com.linkallcloud.um.server.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.activity.party.*;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.activity.sys.IKhSystemConfigActivity;
import com.linkallcloud.um.activity.sys.IMenuActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.service.party.IKhCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class KhCompanyService
        extends CompanyService<KhCompany, IKhCompanyActivity, KhUser, IKhUserActivity, KhDepartment, IKhDepartmentActivity>
        implements IKhCompanyService {

    @Autowired
    private IKhCompanyActivity khCompanyActivity;

    @Autowired
    private IKhUserActivity khUserActivity;

    @Autowired
    private IYwCompanyActivity ywCompanyActivity;

    @Autowired
    private IYwUserActivity ywUserActivity;

    @Autowired
    private IKhDepartmentActivity khDepartmentActivity;

    @Autowired
    private IKhRoleActivity khRoleActivity;

    @Autowired
    private IAreaActivity areaActivity;

    @Autowired
    private IKhSystemConfigActivity khSystemConfigActivity;

    @Autowired
    private IApplicationActivity applicationActivity;

    @Autowired
    private IMenuActivity menuActivity;

    @Override
    protected IKhUserActivity getUserActivity() {
        return khUserActivity;
    }

    @Override
    public IKhCompanyActivity activity() {
        return khCompanyActivity;
    }

    @Override
    protected IKhDepartmentActivity getDepartmentActivity() {
        return khDepartmentActivity;
    }


    public List<KhCompany> countByArea4id(Trace t, KhCompany entity) {
        return activity().countByArea4id(t, entity);
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
        return activity().addApps(t, id, uuid, appUuidIds);
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
        return activity().removeApps(t, id, uuid, appUuidIds);
    }

    @Override
    public Long getCompanyAreaRootIdBySystemConfig(Trace t, Long companyId) {
        return activity().getCompanyAreaRootIdBySystemConfig(t, companyId);
    }

    @Override
    public Tree findCompanyValidMenuTree(Trace t, Long companyId, Long appId) {
        return activity().findCompanyValidMenuTree(t, companyId, appId);
    }

    @Override
    public List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId) {
        return activity().findCompanyValidMenus(t, companyId, appId);
    }

    @Override
    public PermedAreaVo findCompanyValidAreaResource(Trace t, Long companyId, Long appId) {
        return activity().findCompanyValidAreaResource(t, companyId, appId);
    }
}
