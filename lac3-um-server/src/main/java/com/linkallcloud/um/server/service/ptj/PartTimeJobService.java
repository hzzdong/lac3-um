package com.linkallcloud.um.server.service.ptj;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.um.activity.ptj.IPartTimeJobActivity;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.ptj.PartTimeJob;
import com.linkallcloud.um.service.ptj.IPartTimeJobService;

@Module(name = "兼职")
@Transactional(readOnly = true)
public abstract class PartTimeJobService<T extends PartTimeJob, U extends User, A extends IPartTimeJobActivity<T, U>>
		extends BaseService<T, A> implements IPartTimeJobService<T> {

	@Transactional(readOnly = false)
	@Override
	public T add(Trace t, Sid userId, Sid destOrgId, String remark) {
		return activity().add(t, userId, destOrgId, remark);
	}

}
