package com.linkallcloud.um.exception;

public class AuthException extends UmException {
	private static final long serialVersionUID = 2996150178778687298L;

	public AuthException() {
		super("Account", "账号验证错误");
	}

	public AuthException(String message) {
		super("Account", message);
	}

	public AuthException(String code, String message) {
		super(code, message);
	}

	public AuthException(String code, Object[] args) {
		super(code, args);
	}

	public AuthException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public AuthException(String code, Throwable cause) {
		super(code, cause);
	}

	public AuthException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
