package com.linkallcloud.um.server.activity.ptj;

import java.util.List;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.activity.ptj.IPartTimeJobActivity;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.ptj.PartTimeJob;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.server.dao.ptj.IPartTimeJobDao;

public abstract class PartTimeJobActivity<T extends PartTimeJob, U extends User, D extends IPartTimeJobDao<T>>
		extends BaseActivity<T, D> implements IPartTimeJobActivity<T, U> {

	@Override
	public T add(Trace t, Sid userId, Sid destOrgId, String remark) {
		U user = fetchUser(t, userId.getId(), userId.getUuid());
		if (user == null) {
			throw new ArgException("参数错误");
		}

		if (user.getParentClass().equals(destOrgId.getCode()) && user.getParentId().equals(destOrgId.getId())) {
			throw new ArgException("兼职机构必须选择非归属机构");
		}

		Org srcOrg = fetchOrg(t, user.getParentId(), user.getParentClass());
		if (srcOrg == null) {
			throw new ArgException("参数错误");
		}

		Org destOrg = fetchOrg(t, destOrgId.getId(), destOrgId.getCode());
		if (destOrg == null) {
			throw new ArgException("参数错误");
		}

		boolean userHaveDestOrgPartTimeJob = haveDestOrgPartTimeJob(t, userId, destOrgId);
		if (userHaveDestOrgPartTimeJob) {
			throw new ArgException("当前用户已经在此机构兼职");
		}

		T entity = Mirror.me(getDomainClass()).born(user, srcOrg, destOrg);
		entity.setRemark(remark);
		super.insert(t, entity);
		return entity;
	}

	private boolean haveDestOrgPartTimeJob(Trace t, Sid userId, Sid destOrgId) {
		List<T> haveOrgs = findUserPartTimeJobOrgs(t, userId, destOrgId);
		if (haveOrgs != null && haveOrgs.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<T> findUserPartTimeJobOrgs(Trace t, Sid user, Sid destOrg) {
		Query query = new Query();
		query.addRule(new Equal("userId", user.getId()));
		query.addRule(new Equal("destOrgId", destOrg.getId()));
		query.addRule(new Equal("destOrgClass", destOrg.getCode()));
		return this.find(t, query);
	}

	protected abstract U fetchUser(Trace t, Long userId, String userUuid);

	protected abstract Org fetchOrg(Trace t, Long orgId, String orgClass);

}
