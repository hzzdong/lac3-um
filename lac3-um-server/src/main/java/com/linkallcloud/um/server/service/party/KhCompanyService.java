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
import com.linkallcloud.um.activity.party.IKhUserActivity;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.activity.sys.IYwSystemConfigActivity;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.server.utils.UmTools;
import com.linkallcloud.um.service.party.IKhCompanyService;

@Service
@Transactional(readOnly = true)
public class KhCompanyService extends
		CompanyService<KhCompany, IKhCompanyActivity, KhUser, IKhUserActivity, KhDepartment, IKhDepartmentActivity>
		implements IKhCompanyService {

	@Autowired
	private IKhCompanyActivity khCompanyActivity;

	@Autowired
	private IKhUserActivity khUserActivity;

	@Autowired
	private IKhDepartmentActivity khDepartmentActivity;

	@Autowired
	private IYwCompanyActivity ywCompanyActivity;

	@Autowired
	private IYwUserActivity ywUserActivity;

	@Autowired
	private IYwSystemConfigActivity ywSystemConfigActivity;

	@Autowired
	private IAreaActivity areaActivity;

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

	@Override
	public List<KhCompany> find(Trace t, Query query) {
		try {
			UmTools.addCnds4CustomerManageMode(t, query, ywSystemConfigActivity, ywCompanyActivity, ywUserActivity,
					areaActivity);
		} catch (BaseException e) {
			return null;
		}
		return super.find(t, query);
	}

	@Override
	public Page<KhCompany> findPage(Trace t, Page<KhCompany> page) {
		try {
			UmTools.addCnds4CustomerManageMode(t, page, ywSystemConfigActivity, ywCompanyActivity, ywUserActivity,
					areaActivity);
		} catch (BaseException e) {
			return page;
		}
		return super.findPage(t, page);
	}

	@Override
	public Page<KhCompany> findPage4Select(Trace t, Page<KhCompany> page) {
		try {
			UmTools.addCnds4CustomerManageMode(t, page, ywSystemConfigActivity, ywCompanyActivity, ywUserActivity,
					areaActivity);
		} catch (BaseException e) {
			return page;
		}
		return super.findPage4Select(t, page);
	}

}
