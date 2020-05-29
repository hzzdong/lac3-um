package com.linkallcloud.um.server.activity.party;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.IDepartmentActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.server.dao.party.ICompanyDao;
import com.linkallcloud.um.server.dao.party.IDepartmentDao;
import com.linkallcloud.um.server.dao.party.IUserDao;

public abstract class DepartmentActivity<D extends Department, DD extends IDepartmentDao<D>, U extends User, UD extends IUserDao<U>, C extends Company, CD extends ICompanyDao<C>>
		extends OrgActivity<D, DD, U, UD> implements IDepartmentActivity<D> {

	public DepartmentActivity() {
		super();
	}

	protected abstract CD getCompanyDao();

	@Override
	public List<D> findCompanyDepartments(Trace t, Long companyId) {
		return dao().findCompanyDepartments(t, companyId);
	}

	@Override
	public List<D> findCompanyDirectDepartments(Trace t, Long companyId) {
		return dao().findCompanyDirectDepartments(t, companyId);
	}

	@Override
	public List<D> findDepartmentsByParentDepartmentCode(Trace t, String parentDepartmentCode) {
		return dao().findDepartmentsByParentDepartmentCode(t, parentDepartmentCode);
	}

	@Override
	public List<D> findDirectDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId) {
		return dao().findDirectDepartmentsByParentDepartmentId(t, parentDepartmentId);
	}

	@Override
	protected void before(Trace t, boolean isNew, D entity) {
		super.before(t, isNew, entity);
		check(t, isNew, entity);// 检查：一个单位下只允许一个管理部门。
		dealFullName(t, isNew, entity);
		if (isNew) {
			if (entity.isTopParent()) {
				entity.setLevel(1);
			} else {
				D parent = dao().fetchById(t, entity.getParentId());
				if (parent != null) {
					entity.setLevel(parent.getLevel() + 1);
				}
			}
		} else {// 修改
			// updateCodeIfModifiedParent(t, entity);
		}
	}

	protected abstract void dealFullName(Trace t, boolean isNew, D entity);

	/**
	 * 检查：一个单位下只允许一个管理部门(type=12)。
	 *
	 * @param t
	 * @param isNew
	 * @param entity
	 * @throws BaseRuntimeException
	 */
	protected void check(Trace t, boolean isNew, D entity) {
		if (entity.getType() == 12) {
			Query query = new Query();
			query.addRule(new Equal("companyId", entity.getCompanyId()));
			query.addRule(new Equal("type", 12));

			List<D> dbEntities = find(t, query);
			if (isNew || entity.getId() == null) {
				if ((dbEntities != null) && (dbEntities.size() > 0)) {
					throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "机构类型错误：一个单位下只允许一个管理部门。");
				}
			} else {
				if ((dbEntities != null) && (dbEntities.size() > 1)) {
					throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "机构类型错误：一个单位下只允许一个管理部门。");
				}
				if ((dbEntities != null) && (dbEntities.size() == 1)
						&& (!dbEntities.get(0).getId().equals(entity.getId()))) {
					throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "机构类型错误：一个单位下只允许一个管理部门。");
				}
			}
		}
	}

	@Override
	protected void updateCodeIfModifiedParent(Trace t, D entity) {
		boolean parentChanged = false;
		D dbEntity = dao().fetchById(t, entity.getId());
		if (dbEntity.isTopParent()) {
			if (!entity.isTopParent()) {
				parentChanged = true;
			}
		} else {
			if (!dbEntity.getParentId().equals(entity.getParentId())) {
				parentChanged = true;
			}
		}

		if (parentChanged) {
			parseDepartmentCode(t, entity);
			updateCode(t, entity.getId(), entity.getCode());
		}
	}

	private void parseDepartmentCode(Trace t, D entity) {
		if (entity.isTopParent()) {// 直接挂在公司节点下面
			C company = getCompanyDao().fetchById(t, entity.getCompanyId());
			if (company == null) {
				throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "companyId参数错误。");
			}
			entity.setCode(Domains.genDomainCode(company, entity));
		} else {
			D department = dao().fetchById(t, entity.getParentId());
			if (department != null) {
				entity.setCode(Domains.genDomainCode(department, entity));
			} else {
				throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
			}
		}
	}

	@Override
	protected void after(Trace t, boolean isNew, D entity) {
		super.after(t, isNew, entity);
		if (isNew) {// 新增
			parseDepartmentCode(t, entity);
			updateCode(t, entity.getId(), entity.getCode());
		}
	}

}
