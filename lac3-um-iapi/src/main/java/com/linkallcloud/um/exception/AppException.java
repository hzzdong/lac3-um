package com.linkallcloud.um.exception;

public class AppException extends UmException {
	private static final long serialVersionUID = -5425793514553946580L;

	public AppException() {
		super("App", "应用错误");
	}

	public AppException(String message) {
		super("App", message);
	}

	public AppException(String code, String message) {
		super(code, message);
	}

	public AppException(String code, Object[] args) {
		super(code, args);
	}

	public AppException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public AppException(String code, Throwable cause) {
		super(code, cause);
	}

	public AppException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
