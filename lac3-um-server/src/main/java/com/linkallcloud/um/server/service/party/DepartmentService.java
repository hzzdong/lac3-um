package com.linkallcloud.um.server.service.party;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.activity.party.IDepartmentActivity;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.service.party.IDepartmentService;

@Transactional(readOnly = true)
public abstract class DepartmentService<T extends Department, DA extends IDepartmentActivity<T>, U extends User, UA extends IUserActivity<U>, C extends Company, CA extends ICompanyActivity<C>>
        extends OrgService<T, DA, U, UA> implements IDepartmentService<T> {

//    @Autowired
//    private IAreaActivity areaActivity;

    protected abstract CA getCompanyActivity();

    @Override
    public List<T> findCompanyDepartments(Trace t, Long companyId) {
        return activity().findCompanyDepartments(t, companyId);
    }

    @Override
    public List<T> findCompanyDirectDepartments(Trace t, Long companyId) {
        return activity().findCompanyDirectDepartments(t, companyId);
    }

    @Override
    public List<T> findDepartmentsByParentDepartmentCode(Trace t, String parentDepartmentCode) {
        return activity().findDepartmentsByParentDepartmentCode(t, parentDepartmentCode);
    }

    @Override
    public List<T> findDirectDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId) {
        return activity().findDirectDepartmentsByParentDepartmentId(t, parentDepartmentId);
    }

    @Transactional(readOnly = false)
	@Override
	public boolean updateStatus(Trace t, int status, Long id, String uuid) {
		if(Domain.STATUS_NORMAL != status) {
			getUserActivity().updateStatusByDepartment(t, status, id);
		}
		return super.updateStatus(t, status, id, uuid);
	}
	

}
