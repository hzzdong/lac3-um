package com.linkallcloud.um.domain.ptj;

import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.User;

@ShowName("兼职")
public class YwPartTimeJob extends PartTimeJob {
	private static final long serialVersionUID = 7963609431046665884L;

	public YwPartTimeJob() {
		super();
	}

	public YwPartTimeJob(User user, Org srcOrg, Org destOrg) {
		super(user, srcOrg, destOrg);
	}

}
