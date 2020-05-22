package com.linkallcloud.um.server.service.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.um.activity.party.IKhCompanyActivity;
import com.linkallcloud.um.activity.party.IKhDepartmentActivity;
import com.linkallcloud.um.activity.party.IKhRoleActivity;
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.server.utils.UmTools;
import com.linkallcloud.um.service.party.IKhUserService;

@Service
@Transactional(readOnly = true)
public class KhUserService extends
		UserService<KhUser, IKhUserActivity, KhDepartment, IKhDepartmentActivity, KhCompany, IKhCompanyActivity, KhRole, IKhRoleActivity>
		implements IKhUserService {

	@Autowired
	private IKhUserActivity khUserActivity;

	@Autowired
	private IKhDepartmentActivity khDepartmentActivity;

	@Autowired
	private IKhCompanyActivity khCompanyActivity;

	@Autowired
	private IKhRoleActivity khRoleActivity;

    @Autowired
    private IYwCompanyActivity ywCompanyActivity;

    @Autowired
    private IYwUserActivity ywUserActivity;

    @Autowired
    private IAreaActivity areaActivity;

	@Override
	public IKhUserActivity activity() {
		return khUserActivity;
	}

	@Override
	protected IKhDepartmentActivity getDepartmentActivity() {
		return khDepartmentActivity;
	}

	@Override
	protected IKhCompanyActivity getCompanyActivity() {
		return khCompanyActivity;
	}

	@Override
	protected IKhRoleActivity getRoleActivity() {
		return khRoleActivity;
	}

	@Override
	public List<KhUser> find(Trace t, Query query) {
		try {
			UmTools.addAreaCnds4YwUserAppPermission(t, query, ywCompanyActivity, ywUserActivity, areaActivity);
		} catch (BaseException e) {
			return null;
		}
		return super.find(t, query);
	}

	@Override
	public Page<KhUser> findPage(Trace t, Page<KhUser> page) {
		try {
			UmTools.addAreaCnds4YwUserAppPermission(t, page, ywCompanyActivity, ywUserActivity, areaActivity);
		} catch (BaseException e) {
			return page;
		}
		return super.findPage(t, page);
	}

	@Override
	public Page<KhUser> findPage4Select(Trace t, Page<KhUser> page) {
		try {
			UmTools.addAreaCnds4YwUserAppPermission(t, page, ywCompanyActivity, ywUserActivity, areaActivity);
		} catch (BaseException e) {
			return page;
		}
		return super.findPage4Select(t, page);
	}

}
