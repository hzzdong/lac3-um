package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum SignAlg implements EnumBase<Integer> {

    MD5(0, "MD5"), SHA1(1, "SHA1");

    private Integer code;
    private String message;

    SignAlg(Integer code, String message) {
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

    public static SignAlg get(Integer code) {
        for (SignAlg ul : values()) {
            if (ul.getCode().equals(code)) {
                return ul;
            }
        }
        return null;
    }
}
