package com.linkallcloud.um.server.manager.ptj;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.ptj.PartTimeJob;
import com.linkallcloud.um.iapi.ptj.IPartTimeJobManager;
import com.linkallcloud.um.service.ptj.IPartTimeJobService;

public abstract class PartTimeJobManager<T extends PartTimeJob, S extends IPartTimeJobService<T>>
		extends BaseManager<T, S> implements IPartTimeJobManager<T> {

	@Override
	public T add(Trace t, Sid user, Sid destOrg, String remark) {
		return service().add(t, user, destOrg, remark);
	}

}
