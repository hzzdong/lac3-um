package com.linkallcloud.um.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhUserService extends IUserService<KhUser> {

	Page<KhUser> page4UnRole4Yw(Trace t, Page<KhUser> page);

	Page<KhUser> page4Role4Yw(Trace t, Page<KhUser> page);

}
