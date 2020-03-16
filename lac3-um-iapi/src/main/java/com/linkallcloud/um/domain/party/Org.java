package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("组织")
public abstract class Org extends Party {
	private static final long serialVersionUID = 529119483421250987L;

	private int level;// 层级

	private String fullName;// 全名，后续做政府和企业两种模式

	public Org() {
		super();
	}

	public Org(Long id, String uuid) {
		super(id, uuid);
	}

	public Org(Long id) {
		super(id);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
