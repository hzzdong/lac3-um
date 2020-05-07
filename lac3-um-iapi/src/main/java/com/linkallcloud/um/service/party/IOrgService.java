package com.linkallcloud.um.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;

public interface IOrgService<T extends Org> extends IPartyService<T> {
	
	boolean addLeader(Trace t, Rel4OrgLeader leader);
	boolean deleteLeader(Trace t, Rel4OrgLeader leader);

//	boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);
//	boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);

}
