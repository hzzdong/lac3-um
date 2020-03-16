package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;

public interface IOrgDao<T extends Org> extends IPartyDao<T> {

	int saveRel4OrgLeader(@Param("t") Trace t, @Param("rels") List<Rel4OrgLeader> rels);

	int deleteRel4OrgLeader(@Param("t") Trace t, @Param("orgId") Long orgId, @Param("userIds") List<Long> userIds);

}
