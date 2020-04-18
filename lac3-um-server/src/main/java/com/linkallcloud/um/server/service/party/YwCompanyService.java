package com.linkallcloud.um.server.service.party;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwDepartmentActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.service.party.IYwCompanyService;

@Service
@Transactional(readOnly = true)
public class YwCompanyService extends
		CompanyService<YwCompany, IYwCompanyActivity, YwUser, IYwUserActivity, YwDepartment, IYwDepartmentActivity>
		implements IYwCompanyService {

	@Autowired
	private IYwCompanyActivity ywwCompanyActivity;

	@Autowired
	private IYwUserActivity ywUserActivity;

	@Autowired
	private IYwDepartmentActivity ywDepartmentActivity;

//    @Autowired
//    private IYwRoleActivity ywRoleActivity;
//
//    @Autowired
//    private IAreaActivity areaActivity;
//
//    @Autowired
//    private IApplicationActivity applicationActivity;
//
//    @Autowired
//    private IMenuActivity menuActivity;
//
//    @Autowired
//    private IYwSystemConfigActivity ywSystemConfigActivity;

	@Override
	protected IYwUserActivity getUserActivity() {
		return ywUserActivity;
	}

	@Override
	public IYwCompanyActivity activity() {
		return ywwCompanyActivity;
	}

	@Override
	protected IYwDepartmentActivity getDepartmentActivity() {
		return ywDepartmentActivity;
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

	@Override
	public Long getCompanyAreaRootIdBySystemConfig(Trace t, Long companyId) {
		return activity().getCompanyAreaRootIdBySystemConfig(t, companyId);
	}

}
