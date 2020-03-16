package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName(value = "区域", logFields = "id,name,govCode")
public class Area extends TreeDomain {
	private static final long serialVersionUID = -2121584720685444135L;

	private String fullName;

	public Area() {
		super();
	}

	public Area(Long parentId, String name, String govCode, int sort, int level) {
		super(parentId, name, govCode, sort);
		this.setLevel(level);
	}

	public Area(Long parentId, Long id, String name, String govCode, int sort, int level) {
		this(parentId, name, govCode, sort, level);
		this.id = id;
	}
	
	public Area(Area parent, String name, String govCode, int sort, int level) {
		super(parent.getId(), name, govCode, sort);
		this.setLevel(level);
		this.setFullName(parent.getFullName() + name);
	}

	public Area(Area parent, Long id, String name, String govCode, int sort, int level) {
		this(parent, name, govCode, sort, level);
		this.id = id;
	}

	@Override
	protected String getAlias() {
		return String.valueOf(getLevel());
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
