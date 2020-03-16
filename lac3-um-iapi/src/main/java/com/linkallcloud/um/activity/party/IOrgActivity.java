package com.linkallcloud.um.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Org;

import java.util.Map;

public interface IOrgActivity<T extends Org> extends IPartyActivity<T> {

    boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);

    boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds);

}
