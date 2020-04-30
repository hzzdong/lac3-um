package com.linkallcloud.um.iapi.ptj;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.um.domain.ptj.PartTimeJob;

public interface IPartTimeJobManager<T extends PartTimeJob> extends IManager<T> {

	T add(Trace t, Sid user, Sid destOrg, String remark);

}
