package com.linkallcloud.um.exception;

public class ArgException extends UmException {
	private static final long serialVersionUID = 2085227189999440308L;

	public ArgException() {
        super("arg", "参数错误");
    }

    public ArgException(String code) {
        super(code);
    }

    public ArgException(String code, String message) {
        super(code, message);
    }

    public ArgException(String code, Object[] args) {
        super(code, args);
    }

    public ArgException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public ArgException(String code, Throwable cause) {
        super(code, cause);
    }

    public ArgException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
