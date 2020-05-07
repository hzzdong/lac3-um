package com.linkallcloud.um.server.manager.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.iapi.party.IOrgManager;
import com.linkallcloud.um.service.party.IOrgService;
import com.linkallcloud.um.service.party.IUserService;

public abstract class OrgManager<T extends Org, S extends IOrgService<T>, U extends User, US extends IUserService<U>>
		extends PartyManager<T, S> implements IOrgManager<T, U> {

	protected abstract US userService();

	@Override
	public Page<U> leaderPage(Trace t, Page<U> page) {
		return userService().leaderPage(t, page);
	}

	@Override
	public boolean addLeader(Trace t, Rel4OrgLeader leader) {
		return service().addLeader(t, leader);
	}

	@Override
	public boolean deleteLeader(Trace t, Rel4OrgLeader leader) {
		return service().deleteLeader(t, leader);
	}

//	@Override
//	public boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
//			throws BaseRuntimeException {
//		return service().addLeaders(t, orgId, orgUuid, userUuidIds);
//	}
//
//	@Override
//	public boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
//			throws BaseRuntimeException {
//		return service().deleteLeaders(t, orgId, orgUuid, userUuidIds);
//	}

}
