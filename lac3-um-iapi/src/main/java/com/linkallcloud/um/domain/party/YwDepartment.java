package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("运维部门")
public class YwDepartment extends Department {

    private static final long serialVersionUID = -8046926877432461929L;

    /** 状态 0正常, 1禁音, 9删除 */
    // private Integer status;

    public YwDepartment() {
        super();
    }

}
