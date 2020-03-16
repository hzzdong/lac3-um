package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.laclog.ServiceBusiLog;

public class UmServiceLog extends ServiceBusiLog {
	private static final long serialVersionUID = -6207912802597778114L;

	public UmServiceLog() {
		super();
	}

	public UmServiceLog(Long id, String uuid) {
		super(id, uuid);
	}

	public UmServiceLog(Long id) {
		super(id);
	}

}
