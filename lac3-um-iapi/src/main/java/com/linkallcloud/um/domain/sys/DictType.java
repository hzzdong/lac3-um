package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("数据字典类型")
public class DictType extends TreeDomain {
	private static final long serialVersionUID = 8174903020612495491L;

	private Long topParentId;// 本字段表示本类型所在顶层类型ID

	// private List<DictType> children;// 查询字段，子类型
	// private List<Dict> dicts;// 查询字段，类型下的具体字典数据

	public DictType() {
		super();
	}

	public DictType(Long parentId, String name, String govCode, int sort) {
		super(parentId, name, govCode, sort);
	}

	public Long getTopParentId() {
		return topParentId;
	}

	public void setTopParentId(Long topParentId) {
		this.topParentId = topParentId;
	}

}
