package com.linkallcloud.um.domain.sys;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.lang.Strings;

@ShowName(value = "系统配置", logFields = "id")
public class KhSystemConfig extends Domain {
	private static final long serialVersionUID = -5647736641625613721L;

	private Long companyId;

	private String key;// 见com.linkallcloud.um.constant.Consts
	private String name;
	private String value;
	private String remark;

	public KhSystemConfig() {
		super();
	}

	public KhSystemConfig(String key, String name, String value, String remark) {
		super();
		this.key = key;
		this.name = name;
		this.value = value;
		this.remark = remark;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<NameValue> parse() {
		if (!Strings.isBlank(this.getValue())) {
			return JSON.parseObject(this.getValue(), new TypeReference<List<NameValue>>() {
			});
		}
		return null;
	}

}
