package com.linkallcloud.um.server.service.party;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.activity.party.IOrgActivity;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.service.party.IOrgService;

@Transactional(readOnly = true)
public abstract class OrgService<T extends Org, OA extends IOrgActivity<T>, U extends User, UA extends IUserActivity<U>>
		extends PartyService<T, OA> implements IOrgService<T> {

	protected Mirror<U> userMirror;

	@SuppressWarnings("unchecked")
	public OrgService() {
		super();
		try {
			userMirror = Mirror.me((Class<U>) Mirror.getTypeParams(getClass())[2]);
		} catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("!!!Fail to get TypeParams for self!", e);
			}
		}
	}

	protected Class<U> getUserClass() {
		return (null == userMirror) ? null : userMirror.getType();
	}

	protected abstract UA getUserActivity();

	@Transactional(readOnly = false)
	@Override
	public boolean addLeader(Trace t, Rel4OrgLeader leader) {
		if (leader.getUserId() == null || leader.getOrgId() == null || Strings.isBlank(leader.getOrgType())) {
			throw new ArgException("参数错误");
		}
		return activity().addLeader(t, leader);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean deleteLeader(Trace t, Rel4OrgLeader leader) {
		if (leader.getUserId() == null || leader.getOrgId() == null || Strings.isBlank(leader.getOrgType())) {
			throw new ArgException("参数错误");
		}
		return activity().deleteLeader(t, leader);
	}

//	@Transactional(readOnly = false)
//	@Override
//	public boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds) {
//		return activity().deleteLeaders(t, orgId, orgUuid, userUuidIds);
//	}
//
//	@Transactional(readOnly = false)
//	@Override
//	@ServLog(db = true, desc = "批量给机构([(${orgId})])设置领导班子成员([(${userUuidIds})]), TID:[(${tid})]")
//	public boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds) {
//		return activity().addLeaders(t, orgId, orgUuid, userUuidIds);
//	}

	// @Override
	// protected void before(Trace t, boolean isNew, T entity) throws
	// BaseRuntimeException {
	// super.before(t, isNew, entity);
	// if (isNew) {
	// if (entity.isTopParent()) {
	// entity.setLevel(1);
	// } else {
	// T parent = dao().fetchById(t, entity.getParentId());
	// if (parent != null) {
	// entity.setLevel(parent.getLevel() + 1);
	// }
	// }
	// } else {// 修改
	// updateCodeIfModifiedParent(t, entity);
	// }
	// }
	//
	// private void updateCodeIfModifiedParent(Trace t, T entity) {
	// boolean parentChanged = false;
	// T dbEntity = dao().fetchById(t, entity.getId());
	// if (dbEntity.isTopParent()) {
	// if (!entity.isTopParent()) {
	// parentChanged = true;
	// }
	// } else {
	// if (!dbEntity.getParentId().equals(entity.getParentId())) {
	// parentChanged = true;
	// }
	// }
	//
	// if (parentChanged) {
	// if (entity.isTopParent()) {
	// entity.setCode(Domains.genDomainCode(null, entity));
	// } else {
	// T parent = dao().fetchById(t, entity.getParentId());
	// if (parent != null) {
	// entity.setCode(Domains.genDomainCode(parent, entity));
	// } else {
	// throw new BaseRuntimeException(BaseRuntimeException.RUNTIME_EXCEPTION,
	// "parentId参数错误。");
	// }
	// }
	// updateCode(t, entity.getId(), entity.getCode());
	// }
	// }
	//
	// @Override
	// protected void after(Trace t, boolean isNew, T entity) throws
	// BaseRuntimeException {
	// super.after(t, isNew, entity);
	// if (isNew) {// 新增
	// if (entity.isTopParent()) {
	// entity.setCode(Domains.genDomainCode(null, entity));
	// } else {
	// T parent = dao().fetchById(t, entity.getParentId());
	// if (parent != null) {
	// entity.setCode(Domains.genDomainCode(parent, entity));
	// } else {
	// throw new BaseRuntimeException(BaseRuntimeException.RUNTIME_EXCEPTION,
	// "parentId参数错误。");
	// }
	// }
	// updateCode(t, entity.getId(), entity.getCode());
	// }
	// }

}
