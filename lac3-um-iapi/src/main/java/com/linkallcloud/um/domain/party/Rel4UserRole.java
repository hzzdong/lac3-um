package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.Domain;

public class Rel4UserRole extends Domain {
	private static final long serialVersionUID = -683413935477448765L;

	private Long userId;

	private Long roleId;

	public Rel4UserRole(Long userId, Long roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public Rel4UserRole() {
		super();
	}

	public Rel4UserRole(Long id, String uuid) {
		super(id, uuid);
	}

	public Rel4UserRole(Long id) {
		super(id);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
