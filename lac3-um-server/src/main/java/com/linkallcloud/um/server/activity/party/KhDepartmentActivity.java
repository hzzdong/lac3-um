package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.um.activity.party.IKhDepartmentActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.server.dao.party.IKhCompanyDao;
import com.linkallcloud.um.server.dao.party.IKhDepartmentDao;
import com.linkallcloud.um.server.dao.party.IKhUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KhDepartmentActivity extends DepartmentActivity<KhDepartment, IKhDepartmentDao, KhUser, IKhUserDao, KhCompany, IKhCompanyDao> implements IKhDepartmentActivity {

    @Autowired
    private IKhDepartmentDao khDepartmentDao;

    @Autowired
    private IKhUserDao khUserDao;

    @Autowired
    private IKhCompanyDao khCompanyDao;

    public KhDepartmentActivity() {
        super();
    }

    @Override
    protected IKhCompanyDao getCompanyDao() {
        return khCompanyDao;
    }

    @Override
    protected IKhUserDao getUserDao() {
        return khUserDao;
    }

    @Override
    public IKhDepartmentDao dao() {
        return khDepartmentDao;
    }
}
