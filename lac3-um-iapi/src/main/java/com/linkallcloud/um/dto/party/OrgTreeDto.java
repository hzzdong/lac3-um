package com.linkallcloud.um.dto.party;

import java.util.List;

public class OrgTreeDto extends OrgDto {
	private static final long serialVersionUID = 7756517599464922076L;

	private List<OrgTreeDto> children;

	public OrgTreeDto() {
		super();
	}

	public OrgTreeDto(Long id, String uuid) {
		super(id, uuid);
	}

	public OrgTreeDto(Long id) {
		super(id);
	}

	public List<OrgTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<OrgTreeDto> children) {
		this.children = children;
	}

}
