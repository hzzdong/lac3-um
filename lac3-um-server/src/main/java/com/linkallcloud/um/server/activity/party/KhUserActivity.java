package com.linkallcloud.um.server.activity.party;

import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.server.dao.party.*;
import com.linkallcloud.um.server.dao.sys.IAreaDao;
import com.linkallcloud.um.server.utils.UmTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KhUserActivity extends UserActivity<KhUser, IKhUserDao, KhDepartment, IKhDepartmentDao, KhCompany, IKhCompanyDao, KhRole, IKhRoleDao> implements IKhUserActivity {

    @Autowired
    private IKhUserDao khUserDao;

    @Autowired
    private IKhDepartmentDao khDepartmentDao;

    @Autowired
    private IKhCompanyDao khCompanyDao;

    @Autowired
    private IKhRoleDao khRoleDao;

    @Autowired
    private IYwCompanyDao ywCompanyDao;

    @Autowired
    private IYwUserDao ywUserDao;

    @Autowired
    private IAreaDao areaDao;

    public KhUserActivity() {
        super();
    }

    @Override
    public IKhUserDao dao() {
        return khUserDao;
    }

    @Override
    protected IKhDepartmentDao getDepartmentDao() {
        return khDepartmentDao;
    }

    @Override
    protected IKhCompanyDao getCompanyDao() {
        return khCompanyDao;
    }

    @Override
    protected IKhRoleDao getRoleDao() {
        return khRoleDao;
    }

    /**
     * 根据user的区域权限，查找用户
     *
     * @param t
     * @param page 必须包含ywUserId,appId参数条件
     * @return
     */
    @Override
    public Page<KhUser> findPage(Trace t, Page<KhUser> page) {
        try {
            UmTools.addAreaCnds4YwUserAppPermission(t, page, ywCompanyDao, ywUserDao, areaDao);
        } catch (BaseException e) {
            return page;
        }
        return super.findPage(t, page);
    }

    /**
     * 根据user的区域权限，查找用户
     *
     * @param t
     * @param page 必须包含ywUserId,appId参数条件
     * @return
     */
    @Override
    public Page<KhUser> findPage4Select(Trace t, Page<KhUser> page) {
        try {
            UmTools.addAreaCnds4YwUserAppPermission(t, page, ywCompanyDao, ywUserDao, areaDao);
        } catch (BaseException e) {
            return page;
        }
        return super.findPage4Select(t, page);
    }

    @Override
    public Page<KhUser> findSelfUserPage(Trace t, Page<KhUser> page) {
        page.checkPageParameters();
        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<KhUser> list = dao().findSelfUserPage(t, page);
            if (list instanceof com.github.pagehelper.Page) {
                page.setRecordsTotal(((com.github.pagehelper.Page<KhUser>) list).getTotal());
                page.checkPageParameters();
                page.setRecordsFiltered(page.getRecordsTotal());
                page.addDataAll(list);
            }
            return page;
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public Page<KhUser> findPermedSelfUserPage(Trace t, Page<KhUser> page) {
        page.checkPageParameters();
        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<KhUser> list = dao().findPermedSelfUserPage(t, page);
            if (list instanceof com.github.pagehelper.Page) {
                page.setRecordsTotal(((com.github.pagehelper.Page<KhUser>) list).getTotal());
                page.checkPageParameters();
                page.setRecordsFiltered(page.getRecordsTotal());
                page.addDataAll(list);
            }
            return page;
        } finally {
            PageHelper.clearPage();
        }
    }

	@Override
	protected String departmentAdminRoleCode() {
		return "KhRole_sys_dept";
	}

}
