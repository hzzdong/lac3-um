package com.linkallcloud.um.domain.ptj;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.User;

@ShowName("兼职")
public abstract class PartTimeJob extends Domain {
	private static final long serialVersionUID = 4828674035726848084L;

	private Long srcCompanyId;// 原公司ID
	private Long srcOrgId;// 原组织机构ID
	private String srcOrgClass;// 原组织机构类型
	private String srcOrgName;// 原组织机构名称

	private Long userId;// 用户ID
	private String userUuid;// 用户uuid
	private String userName;// 用户姓名

	private Long destCompanyId;// 兼职公司ID
	private Long destOrgId;// 兼职组织机构ID
	private String destOrgClass;// 兼职组织机构类型
	private String destOrgName;// 兼职组织机构名称

	private String remark;

	public PartTimeJob() {
		super();
	}

	public PartTimeJob(User user, Org srcOrg, Org destOrg) {
		super();
		this.userId = user.getId();
		this.userUuid = user.getUuid();
		this.userName = user.getName();

		this.srcCompanyId = srcOrg.myCompanyId();
		this.srcOrgId = srcOrg.getId();
		this.srcOrgName = srcOrg.getOrgFullName();
		this.srcOrgClass = srcOrg.getClass().getSimpleName();

		this.destCompanyId = destOrg.myCompanyId();
		this.destOrgId = destOrg.getId();
		this.destOrgName = destOrg.getOrgFullName();
		this.destOrgClass = destOrg.getClass().getSimpleName();
	}

	public Long getSrcCompanyId() {
		return srcCompanyId;
	}

	public void setSrcCompanyId(Long srcCompanyId) {
		this.srcCompanyId = srcCompanyId;
	}

	public Long getSrcOrgId() {
		return srcOrgId;
	}

	public void setSrcOrgId(Long srcOrgId) {
		this.srcOrgId = srcOrgId;
	}

	public String getSrcOrgClass() {
		return srcOrgClass;
	}

	public void setSrcOrgClass(String srcOrgClass) {
		this.srcOrgClass = srcOrgClass;
	}

	public String getSrcOrgName() {
		return srcOrgName;
	}

	public void setSrcOrgName(String srcOrgName) {
		this.srcOrgName = srcOrgName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getDestCompanyId() {
		return destCompanyId;
	}

	public void setDestCompanyId(Long destCompanyId) {
		this.destCompanyId = destCompanyId;
	}

	public Long getDestOrgId() {
		return destOrgId;
	}

	public void setDestOrgId(Long destOrgId) {
		this.destOrgId = destOrgId;
	}

	public String getDestOrgClass() {
		return destOrgClass;
	}

	public void setDestOrgClass(String destOrgClass) {
		this.destOrgClass = destOrgClass;
	}

	public String getDestOrgName() {
		return destOrgName;
	}

	public void setDestOrgName(String destOrgName) {
		this.destOrgName = destOrgName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
