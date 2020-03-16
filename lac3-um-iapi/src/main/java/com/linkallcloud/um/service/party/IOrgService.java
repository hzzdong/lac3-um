package com.linkallcloud.um.service.party;

import java.util.Map;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Org;

public interface IOrgService<T extends Org> extends IPartyService<T> {

	boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);

	boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);

}
