package com.linkallcloud.um.exception;

import com.linkallcloud.core.exception.BizException;

public class UmException extends BizException {
	private static final long serialVersionUID = 597264128386997381L;

	public UmException() {
		super("um", "UM系统错误");
	}

	public UmException(String message) {
		super("um", message);
	}

	public UmException(String code, String message) {
		super(code, message);
	}

	public UmException(String code, Object[] args) {
		super(code, args);
	}

	public UmException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public UmException(String code, Throwable cause) {
		super(code, cause);
	}

	public UmException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
