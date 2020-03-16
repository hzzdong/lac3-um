package com.linkallcloud.um.server.activity.party;

import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.server.dao.party.IYwCompanyDao;
import com.linkallcloud.um.server.dao.party.IYwDepartmentDao;
import com.linkallcloud.um.server.dao.party.IYwRoleDao;
import com.linkallcloud.um.server.dao.party.IYwUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class YwUserActivity extends UserActivity<YwUser, IYwUserDao, YwDepartment, IYwDepartmentDao, YwCompany, IYwCompanyDao, YwRole, IYwRoleDao> implements IYwUserActivity {

    @Autowired
    private IYwUserDao ywUserDao;

    @Autowired
    private IYwDepartmentDao ywDepartmentDao;

    @Autowired
    private IYwCompanyDao ywCompanyDao;

    @Autowired
    private IYwRoleDao ywRoleDao;

    public YwUserActivity() {
        super();
    }

    @Override
    public IYwUserDao dao() {
        return ywUserDao;
    }

    @Override
    protected IYwDepartmentDao getDepartmentDao() {
        return ywDepartmentDao;
    }

    @Override
    protected IYwCompanyDao getCompanyDao() {
        return ywCompanyDao;
    }

    @Override
    protected IYwRoleDao getRoleDao() {
        return ywRoleDao;
    }

    @Override
    public Page<YwUser> findPermedUserPage(Trace t, Page<YwUser> page) {
        page.checkPageParameters();
        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<YwUser> list = dao().findPermedUserPage(t, page);
            if (list instanceof com.github.pagehelper.Page) {
                page.setRecordsTotal(((com.github.pagehelper.Page<YwUser>) list).getTotal());
                page.checkPageParameters();
                page.setRecordsFiltered(page.getRecordsTotal());
                page.addDataAll(list);
            }
            return page;
        } finally {
            PageHelper.clearPage();
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void cleanOtherUserMobileByUserId(Trace t, String mobile, Long userId) {
        Query query = new Query();
        query.addRule(new Equal("mobileEq", mobile));
        List<YwUser> users = find(t, query);
        if (users != null && users.size() > 0) {
            for (YwUser user : users) {
                if (!user.getId().equals(userId)) {
                    user.setMobile("");
                    user.setPassword(null);
                    user.setSalt(null);
                    dao().update(t, user);
                }
            }
        }
    }

    @Override
    public YwUser findByMobileAndDdStatus(Trace t, String mobile) {
        return dao().findByMobileAndDdStatus(t, mobile);
    }

}
