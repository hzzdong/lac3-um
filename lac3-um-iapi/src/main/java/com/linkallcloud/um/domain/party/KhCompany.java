package com.linkallcloud.um.domain.party;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;

@ShowName("客户单位")
public class KhCompany extends Company {
	private static final long serialVersionUID = -6516840348058773569L;

	private String khTypeCode;// 字典中客户类型的govCode

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

	public KhCompany() {
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

	public Date getBusinessStart() {
		return businessStart;
	}

	public void setBusinessStart(Date businessStart) {
		this.businessStart = businessStart;
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

	public String getKhTypeCode() {
		return khTypeCode;
	}

	public void setKhTypeCode(String khTypeCode) {
		this.khTypeCode = khTypeCode;
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
	public Tree toTreeNode() {
		Tree node = super.toTreeNode();
		node.addAttribute("khTypeCode", this.khTypeCode);
		return node;
	}

}
