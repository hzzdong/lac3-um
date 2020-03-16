package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.lang.Strings;

@ShowName(value = "系统配置", logFields = "id")
public class KhSystemConfig extends Domain {
    private static final long serialVersionUID = -5647736641625613721L;

    private Long companyId;

    private Long rootAreaId;

    private String enableOrgPerm;// on:启用;off:禁用
    private String enableAreaPerm;// on:启用;off:禁用

    private String logo;

    /*
     * 以下非数据库字段
     */
    private String rootAreaName;

    public KhSystemConfig() {
        super();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getRootAreaId() {
        return rootAreaId;
    }

    public void setRootAreaId(Long rootAreaId) {
        this.rootAreaId = rootAreaId;
    }

    public String getEnableOrgPerm() {
        return Strings.isBlank(enableOrgPerm) ? "off" : enableOrgPerm;
    }

    public void setEnableOrgPerm(String enableOrgPerm) {
        this.enableOrgPerm = enableOrgPerm;
    }

    public String getEnableAreaPerm() {
        return Strings.isBlank(enableAreaPerm) ? "off" : enableAreaPerm;
    }

    public void setEnableAreaPerm(String enableAreaPerm) {
        this.enableAreaPerm = enableAreaPerm;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRootAreaName() {
        return rootAreaName;
    }

    public void setRootAreaName(String rootAreaName) {
        this.rootAreaName = rootAreaName;
    }

}
