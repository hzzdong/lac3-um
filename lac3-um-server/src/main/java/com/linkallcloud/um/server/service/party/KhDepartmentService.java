package com.linkallcloud.um.server.service.party;

import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.party.IKhDepartmentActivity;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.service.party.IKhDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class KhDepartmentService
        extends DepartmentService<KhDepartment, IKhDepartmentActivity, KhUser, IKhUserActivity, KhCompany, IKhCompanyActivity>
        implements IKhDepartmentService {

    @Autowired
    private IKhDepartmentActivity khDepartmentActivity;

    @Autowired
    private IKhUserActivity khUserActivity;

    @Autowired
    private IKhCompanyActivity khCompanyActivity;

    @Override
    protected IKhUserActivity getUserActivity() {
        return khUserActivity;
    }

    @Override
    public IKhDepartmentActivity activity() {
        return khDepartmentActivity;
    }

    @Override
    protected IKhCompanyActivity getCompanyActivity() {
        return khCompanyActivity;
    }
}
