package com.linkallcloud.um.dto.es;

import java.io.Serializable;

public class EsResultTotal implements Serializable {
    private static final long serialVersionUID = 7725893993161504413L;

    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
