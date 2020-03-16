package com.linkallcloud.um.face.area;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class ParentCodeLevelAreaRequest extends FaceRequest {
	private static final long serialVersionUID = 8992915769840690334L;

	private String parentCode;
	private int levelLt;

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public int getLevelLt() {
		return levelLt;
	}

	public void setLevelLt(int levelLt) {
		this.levelLt = levelLt;
	}

}
