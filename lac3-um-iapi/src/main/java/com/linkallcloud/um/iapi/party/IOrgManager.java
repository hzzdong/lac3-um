package com.linkallcloud.um.iapi.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;
import com.linkallcloud.um.domain.party.User;

public interface IOrgManager<T extends Org, U extends User> extends IPartyManager<T> {

	Page<U> leaderPage(Trace t, Page<U> page);

	boolean addLeader(Trace t, Rel4OrgLeader leader);

	boolean deleteLeader(Trace t, Rel4OrgLeader leader);

//	boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
//			throws BaseRuntimeException;
//	boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds)
//			throws BaseRuntimeException;

}
