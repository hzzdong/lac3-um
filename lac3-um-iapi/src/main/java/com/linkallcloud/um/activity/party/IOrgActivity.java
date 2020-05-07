package com.linkallcloud.um.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;

public interface IOrgActivity<T extends Org> extends IPartyActivity<T> {
	
	boolean addLeader(Trace t, Rel4OrgLeader leader);
	boolean deleteLeader(Trace t, Rel4OrgLeader leader);

//    boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);
//    boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);

}
