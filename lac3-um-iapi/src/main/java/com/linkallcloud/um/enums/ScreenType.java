package com.linkallcloud.um.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum ScreenType implements EnumBase<Integer> {

    Pc(1, "电脑"), Pad(2, "平板"), Mobile(3, "手机"), Tv(4, "电视"), Other(5, "其它");

    private Integer code;
    private String message;

    ScreenType(Integer code, String message) {
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

    public static ScreenType get(Integer code) {
        for (ScreenType ul : values()) {
            if (ul.getCode().equals(code)) {
                return ul;
            }
        }
        return null;
    }

}
