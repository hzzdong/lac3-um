package com.linkallcloud.um.iapi.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhRoleManager extends IRoleManager<KhRole, KhUser> {
	
	Tree findPermedMenuTree4SysKhRole(Trace t, Long roleId, Long appId);

}
