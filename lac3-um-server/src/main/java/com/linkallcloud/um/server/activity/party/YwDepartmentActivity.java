package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.um.activity.party.IYwDepartmentActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YwDepartmentActivity extends DepartmentActivity<YwDepartment, IYwDepartmentDao, YwUser, IYwUserDao, YwCompany, IYwCompanyDao> implements IYwDepartmentActivity {

    @Autowired
    private IYwDepartmentDao ywDepartmentDao;

    @Autowired
    private IYwUserDao ywUserDao;

    @Autowired
    private IYwCompanyDao ywCompanyDao;

    @Override
    protected IYwUserDao getUserDao() {
        return ywUserDao;
    }

    @Override
    public IYwDepartmentDao dao() {
        return ywDepartmentDao;
    }

    @Override
    protected IYwCompanyDao getCompanyDao() {
        return ywCompanyDao;
    }

}
