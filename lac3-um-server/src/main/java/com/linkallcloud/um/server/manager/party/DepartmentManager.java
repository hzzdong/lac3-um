package com.linkallcloud.um.server.manager.party;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.IDepartmentManager;
import com.linkallcloud.um.service.party.ICompanyService;
import com.linkallcloud.um.service.party.IDepartmentService;
import com.linkallcloud.um.service.party.IUserService;

public abstract class DepartmentManager<T extends Department, S extends IDepartmentService<T>, U extends User, US extends IUserService<U>, C extends Company, CS extends ICompanyService<C>>
		extends OrgManager<T, S, U, US> implements IDepartmentManager<T, U> {

	protected abstract US userService();

	protected abstract CS companyService();

	@Override
	public List<T> findCompanyDepartments(Trace t, Long companyId) {
		return service().findCompanyDepartments(t, companyId);
	}

	@Override
	public List<T> findZfDepartmentsByCompanyGovCode(Trace t, String companyGoveCode) {
		String virtualDepGovCode = companyGoveCode + "-bm";
		T virtualDep = service().fetchByGovCode(t, virtualDepGovCode);
		if (virtualDep != null) {
			return service().findDepartmentsByParentDepartmentCode(t, virtualDep.getCode());
		} else {// 20190713新增逻辑，区县下没有部门虚拟节点，直接查询子部门
			C company = companyService().fetchByGovCode(t, companyGoveCode);
			return service().findCompanyDepartments(t, company.getId());
		}
	}

	@Override
	public List<T> findZfTopDepartmentsByCompanyGovCode(Trace t, String companyGoveCode) {
		String virtualDepGovCode = companyGoveCode + "-bm";
		T virtualDep = service().fetchByGovCode(t, virtualDepGovCode);
		if (virtualDep != null) {
			return service().findDirectDepartmentsByParentDepartmentId(t, virtualDep.getId());
		} else {// 20190713新增逻辑，区县下没有部门虚拟节点，直接查询子部门
			C company = companyService().fetchByGovCode(t, companyGoveCode);
			return service().findCompanyDirectDepartments(t, company.getId());
		}
	}

	@Override
	public List<T> findCompanyDirectDepartments(Trace t, Long companyId) {
		return service().findCompanyDirectDepartments(t, companyId);
	}

	@Override
	public List<T> findDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId) {
		T dep = service().fetchById(t, parentDepartmentId);
		if (dep != null) {
			return service().findDepartmentsByParentDepartmentCode(t, dep.getCode());
		}
		return null;
	}

	@Override
	public List<T> findDirectDepartmentsByParentDepartmentGovCode(Trace t, String parentDepartmentGoveCode) {
		T dep = service().fetchByGovCode(t, parentDepartmentGoveCode);
		if (dep != null) {
			return service().findDirectDepartmentsByParentDepartmentId(t, dep.getId());
		}
		return null;
	}

	@Override
	public List<T> findDirectDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId) {
		return service().findDirectDepartmentsByParentDepartmentId(t, parentDepartmentId);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updateStatus(Trace t, int status, Long id, String uuid) throws BaseRuntimeException {
		if (status != 0) {
			userService().updateStatusByDepartment(t, status, id);
		}
		return super.updateStatus(t, status, id, uuid);
	}

}
