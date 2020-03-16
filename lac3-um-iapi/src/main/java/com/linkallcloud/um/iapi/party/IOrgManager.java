package com.linkallcloud.um.iapi.party;

import java.util.Map;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.um.domain.party.Org;

public interface IOrgManager<T extends Org> extends IPartyManager<T> {

	boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
			throws BaseRuntimeException;

	boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
			throws BaseRuntimeException;

}
