package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.lang.Strings;

@ShowName("部门")
public abstract class Department extends Org {
	private static final long serialVersionUID = -7578683987298121778L;

	private Long companyId;

	private Long linkUserId;
	private String linkUserName;
	private String linkUserPhone;

	private Integer mdep;// 是否管理部门，1：是；0：否

	public Department() {
		super();
	}

	@Override
	public String codeTag() {
		return "-";
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getLinkUserId() {
		return linkUserId;
	}

	public void setLinkUserId(Long linkUserId) {
		this.linkUserId = linkUserId;
	}

	public String getLinkUserName() {
		return linkUserName;
	}

	public void setLinkUserName(String linkUserName) {
		this.linkUserName = linkUserName;
	}

	public String getLinkUserPhone() {
		return linkUserPhone;
	}

	public void setLinkUserPhone(String linkUserPhone) {
		this.linkUserPhone = linkUserPhone;
	}

	public Integer getMdep() {
		return mdep;
	}

	public void setMdep(Integer mdep) {
		this.mdep = mdep;
	}

	@Override
	protected String getAlias() {
		return "Department";
	}

	@Override
	public String getOrgFullName() {
		String fn = super.getFullName();
		if (Strings.isBlank(fn)) {
			fn = this.getName();
		}
		return fn;
	}

	@Override
	public Tree toTreeNode() {
		Tree treeNode = super.toTreeNode();
		treeNode.setFullName(getOrgFullName());
		treeNode.addAttribute("linkUserId", this.getLinkUserId());
		treeNode.addAttribute("linkUserName", this.getLinkUserName());
		treeNode.addAttribute("linkUserPhone", this.getLinkUserPhone());
		treeNode.addAttribute("code", this.getCode());
		treeNode.addAttribute("mdep", this.getMdep());
		return treeNode;
	}
}
