package com.linkallcloud.um.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhUserActivity extends IUserActivity<KhUser> {

	Page<KhUser> page4UnRole4Yw(Trace t, Page<KhUser> page);

	Page<KhUser> page4Role4Yw(Trace t, Page<KhUser> page);

}
