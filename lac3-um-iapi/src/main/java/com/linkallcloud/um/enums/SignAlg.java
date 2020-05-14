package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum SignAlg implements EnumBase<String> {

	MD5("MD5", "MD5"), SHA1("SHA1", "SHA1");

	private String code;
	private String message;

	SignAlg(String code, String message) {
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

	public static SignAlg get(String code) {
		for (SignAlg ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}
}
