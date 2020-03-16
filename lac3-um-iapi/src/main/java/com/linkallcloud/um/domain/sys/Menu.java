package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;

@ShowName(value = "菜单", logFields = "id,name,govCode")
public class Menu extends TreeDomain {
	private static final long serialVersionUID = 1181542280532338073L;

	private Long appId;

	public Menu() {
		super();
	}

	public Menu(Long id, String uuid) {
		super(id, uuid);
	}

	public Menu(Long id) {
		super(id);
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Override
	public Tree toTreeNode() {
		Tree treeNode =  super.toTreeNode();
		treeNode.setIcon(this.getIco());
		return treeNode;
	}

	@Override
	protected String getAlias() {
		if (this.getType() == 0) {
			return "Menu";
		} else {
			return "Button";
		}
	}

}
