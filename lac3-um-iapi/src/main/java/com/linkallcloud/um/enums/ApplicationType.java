package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum ApplicationType implements EnumBase<Integer> {
	Inner(0, "内部"), Outer(1, "外部");

	private Integer code;
	private String message;

	ApplicationType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static ApplicationType get(Integer code) {
		for (ApplicationType ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
