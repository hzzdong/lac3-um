package com.linkallcloud.um.domain.party;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.lang.Strings;

@ShowName("单位")
public abstract class Company extends Org {
	private static final long serialVersionUID = -4216405193715567536L;

	private Long holidayId;// 假日，工作时间设置采用的父companyId

	private String address;// 单位地址
	private String phone;// 单位电话
	private String fax;// 单位传真

	private String juridical;// 法人/联系人
	private String jphone;// 法人联系方式
	private String jType;// 法人证照类型
	private String jNo;// 法人证照号码

	private String typeCode;// kh_xx:普通单位；kh_qy：企业单位

	private Long areaId;// 所属区域
	private Long area1Id;// 所属区域level1
	private Long area2Id;// 所属区域level2
	private Long area3Id;// 所属区域level3
	private Long area4Id;// 所属区域level4
	private Long area5Id;// 所属区域level5
	private Long area6Id;// 所属区域level6
	private Long area7Id;// 所属区域level7
	private Long area8Id;// 所属区域level8

	@JSONField(format = "yyyy-MM-dd")
	private Date businessStart;// 业务正式上线运营时间

	/*
	 * 企事业单位字段
	 */
	private String certificateType;// 单位证照类型
	private String certificateNo;// 单位证照号码
	private Integer scale;// 单位规模
	private String business;// 经营范围
	private String credentials;// 单位资质

	/*
	 * 以下字段为查询字段
	 */
	private String areaName;

	public Company() {
		super();
		this.businessStart = new Date();
	}

	public void setAreaLevelId(int level, Long areaLevelId) {
		if (level < 1 || level > 8 || areaLevelId == null || areaLevelId.longValue() <= 0) {
			return;
		}
		if (level == 1) {
			this.area1Id = areaLevelId;
		} else if (level == 2) {
			this.area2Id = areaLevelId;
		} else if (level == 3) {
			this.area3Id = areaLevelId;
		} else if (level == 4) {
			this.area4Id = areaLevelId;
		} else if (level == 5) {
			this.area5Id = areaLevelId;
		} else if (level == 6) {
			this.area6Id = areaLevelId;
		} else if (level == 7) {
			this.area7Id = areaLevelId;
		} else if (level == 8) {
			this.area8Id = areaLevelId;
		}
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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Long getArea1Id() {
		return area1Id;
	}

	public void setArea1Id(Long area1Id) {
		this.area1Id = area1Id;
	}

	public Long getArea2Id() {
		return area2Id;
	}

	public void setArea2Id(Long area2Id) {
		this.area2Id = area2Id;
	}

	public Long getArea3Id() {
		return area3Id;
	}

	public void setArea3Id(Long area3Id) {
		this.area3Id = area3Id;
	}

	public Long getArea4Id() {
		return area4Id;
	}

	public void setArea4Id(Long area4Id) {
		this.area4Id = area4Id;
	}

	public Long getArea5Id() {
		return area5Id;
	}

	public void setArea5Id(Long area5Id) {
		this.area5Id = area5Id;
	}

	public Long getArea6Id() {
		return area6Id;
	}

	public void setArea6Id(Long area6Id) {
		this.area6Id = area6Id;
	}

	public Long getArea7Id() {
		return area7Id;
	}

	public void setArea7Id(Long area7Id) {
		this.area7Id = area7Id;
	}

	public Long getArea8Id() {
		return area8Id;
	}

	public void setArea8Id(Long area8Id) {
		this.area8Id = area8Id;
	}

	public Date getBusinessStart() {
		return businessStart;
	}

	public void setBusinessStart(Date businessStart) {
		this.businessStart = businessStart;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	@Override
	public String codeTag() {
		return "#";
	}

	@Override
	protected String getAlias() {
		return "Company";
	}

	public boolean isChildOf(Long parentId) {
		if (this.getCode().indexOf("#" + parentId + "#") != -1) {
			return true;
		}
		if (this.getCode().indexOf(parentId + "#") == 0) {
			return true;
		}
		return false;
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
		treeNode.addAttribute("linkUserId", "");
		treeNode.addAttribute("linkUserName", this.getJuridical());
		treeNode.addAttribute("linkUserPhone", this.getJphone());
		treeNode.addAttribute("code", this.getCode());
		treeNode.addAttribute("areaId", this.getAreaId());
		treeNode.addAttribute("typeCode", this.getTypeCode());
		return treeNode;
	}

	public Long getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

}
