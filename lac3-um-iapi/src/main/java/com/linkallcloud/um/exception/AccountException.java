package com.linkallcloud.um.exception;

public class AccountException extends UmException {
	private static final long serialVersionUID = 4304092590651451292L;

	public AccountException() {
		super("Account", "账号异常");
	}

	public AccountException(String message) {
		super("Account", message);
	}

	public AccountException(String code, String message) {
		super(code, message);
	}

	public AccountException(String code, Object[] args) {
		super(code, args);
	}

	public AccountException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public AccountException(String code, Throwable cause) {
		super(code, cause);
	}

	public AccountException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
