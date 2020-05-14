package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum ApplicationClazz implements EnumBase<Integer> {
	
	Yw(0, "运维"), Kh(1, "客户");

	private Integer code;
	private String message;

	ApplicationClazz(Integer code, String message) {
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

	public static ApplicationClazz get(Integer code) {
		for (ApplicationClazz ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
