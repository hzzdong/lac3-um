package com.linkallcloud.um.iapi.party;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhCompanyManager extends ICompanyManager<KhCompany, KhUser> {

	/**
	 * 统计镇对应的村个数 (按县、镇管理员登录)
	 */
	List<KhCompany> countByArea4id(Trace t, KhCompany entity);

//	List<Tree> findPermedKhCompanyAppMenus(Trace t, Long ywCompanyId, Long khCompanyId, Long appId);

}
