package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName(value = "操作", logFields = "id,name,uri")
public class Operation extends Domain {
	private static final long serialVersionUID = 6556598825525421528L;

	/*
	 * 菜单ID
	 */
	private Long menuId;
	/*
	 * 比如：/area/add;/area/save
	 */
	private String uri;
	/*
	 * 比如：新增区域；保存区域
	 */
	private String name;
	/*
	 * 备注
	 */
	private String remark;

	private Long appId;// 冗余字段：menuId所属的appId
	private String menuGovCode;// 冗余字段：菜单资源的govCode
	private String menuName;// 冗余字段：菜单资源的name

	public Operation() {
		super();
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getMenuGovCode() {
		return menuGovCode;
	}

	public void setMenuGovCode(String menuGovCode) {
		this.menuGovCode = menuGovCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

}
