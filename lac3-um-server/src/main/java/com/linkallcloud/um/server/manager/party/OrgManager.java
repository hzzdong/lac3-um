package com.linkallcloud.um.server.manager.party;

import java.util.Map;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.iapi.party.IOrgManager;
import com.linkallcloud.um.service.party.IOrgService;

public abstract class OrgManager<T extends Org, S extends IOrgService<T>> // , U extends User, US extends IUserService<U>
		extends PartyManager<T, S> implements IOrgManager<T> {

	@Override
	public boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
			throws BaseRuntimeException {
		return service().addLeaders(t, orgId, orgUuid, userUuidIds);
	}

	@Override
	public boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
			throws BaseRuntimeException {
		return service().deleteLeaders(t, orgId, orgUuid, userUuidIds);
	}

}
