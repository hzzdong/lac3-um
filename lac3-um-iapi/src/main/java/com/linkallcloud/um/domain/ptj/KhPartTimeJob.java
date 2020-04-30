package com.linkallcloud.um.domain.ptj;

import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.User;

@ShowName("兼职")
public class KhPartTimeJob extends PartTimeJob {
	private static final long serialVersionUID = 1856233006096586389L;

	public KhPartTimeJob() {
		super();
	}

	public KhPartTimeJob(User user, Org srcOrg, Org destOrg) {
		super(user, srcOrg, destOrg);
	}

}
