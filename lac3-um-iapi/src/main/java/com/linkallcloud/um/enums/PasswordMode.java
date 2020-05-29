package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum PasswordMode implements EnumBase<String> {

	Original("0", "原文"), MD5("MD5", "MD5"), SHA1("SHA1", "SHA1");

	private String code;
	private String message;

	PasswordMode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static PasswordMode get(String code) {
		for (PasswordMode ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}
}
