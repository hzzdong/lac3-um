package com.linkallcloud.um.activity.party;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.KhCompany;

public interface IKhCompanyActivity extends ICompanyActivity<KhCompany> {

	/**
	 * 统计镇对应的村个数 (按县、镇管理员登录)
	 */
	List<KhCompany> countByArea4id(Trace t, KhCompany entity);

}
