package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.laclog.WebBusiLog;

public class UmWebLog extends WebBusiLog {
	private static final long serialVersionUID = 4551689842447942150L;

	public UmWebLog() {
		super();
	}

	public UmWebLog(Long id, String uuid) {
		super(id, uuid);
	}

	public UmWebLog(Long id) {
		super(id);
	}

}
