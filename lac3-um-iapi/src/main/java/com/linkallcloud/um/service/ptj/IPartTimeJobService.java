package com.linkallcloud.um.service.ptj;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.um.domain.ptj.PartTimeJob;

public interface IPartTimeJobService<T extends PartTimeJob> extends IService<T> {
	
	T add(Trace t, Sid user, Sid destOrg, String remark);

}
