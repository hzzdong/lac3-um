package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhUserDao extends IUserDao<KhUser> {

	List<KhUser> page4UnRole4Yw(@Param("t") Trace t, @Param("page") Page<KhUser> page);

	List<KhUser> page4Role4Yw(@Param("t") Trace t, @Param("page") Page<KhUser> page);

}
