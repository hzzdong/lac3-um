package com.linkallcloud.um.activity.ptj;

import java.util.List;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.ptj.PartTimeJob;

public interface IPartTimeJobActivity<T extends PartTimeJob, U extends User> extends IActivity<T> {

	T add(Trace t, Sid user, Sid destOrg, String remark);

	List<T> findUserPartTimeJobOrgs(Trace t, Sid user, Sid destOrg);

}
