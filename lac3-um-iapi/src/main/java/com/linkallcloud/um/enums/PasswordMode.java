package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum PasswordMode implements EnumBase<Integer> {

    Original(0, "原文"), MD5(1, "MD5"), SHA1(2, "SHA1");

    private Integer code;
    private String message;

    PasswordMode(Integer code, String message) {
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

    public static PasswordMode get(Integer code) {
        for (PasswordMode ul : values()) {
            if (ul.getCode().equals(code)) {
                return ul;
            }
        }
        return null;
    }
}
