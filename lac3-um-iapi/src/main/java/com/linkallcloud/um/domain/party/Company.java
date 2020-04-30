package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.lang.Strings;

@ShowName("单位")
public abstract class Company extends Org {
	private static final long serialVersionUID = -4216405193715567536L;

	private Long areaId;// 所属区域

	private String address;// 单位地址
	private String phone;// 单位电话
	private String fax;// 单位传真

	private String juridical;// 法人/联系人
	private String jphone;// 法人联系方式
	private String jType;// 法人证照类型
	private String jNo;// 法人证照号码

	/*
	 * 以下字段为查询字段
	 */
	private String areaName;

	public Company() {
		super();
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getJuridical() {
		return juridical;
	}

	public void setJuridical(String juridical) {
		this.juridical = juridical;
	}

	public String getJphone() {
		return jphone;
	}

	public void setJphone(String jphone) {
		this.jphone = jphone;
	}

	public String getjType() {
		return jType;
	}

	public void setjType(String jType) {
		this.jType = jType;
	}

	public String getjNo() {
		return jNo;
	}

	public void setjNo(String jNo) {
		this.jNo = jNo;
	}

	@Override
	public String codeTag() {
		return "#";
	}

	@Override
	protected String getAlias() {
		return "Company";
	}

	@Override
	public String getOrgFullName() {
		String fn = super.getFullName();
		if (Strings.isBlank(fn)) {
			return this.getName();
		}
		return fn;
	}

	@Override
	public Tree toTreeNode() {
		Tree treeNode = super.toTreeNode();
		treeNode.addAttribute("linkUserId", "");
		treeNode.addAttribute("linkUserName", this.getJuridical());
		treeNode.addAttribute("linkUserPhone", this.getJphone());
		treeNode.addAttribute("code", this.getCode());
		treeNode.addAttribute("areaId", this.getAreaId());
		return treeNode;
	}

}
