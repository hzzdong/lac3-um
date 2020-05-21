package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.KhCompany;

public interface IKhCompanyDao extends ICompanyDao<KhCompany> {

	List<KhCompany> findNoBonusPointsAccountVillages(@Param("t") Trace t);

	/**
	 * 统计镇对应的村个数 (按县、镇管理员登录)
	 */
	List<KhCompany> countByArea4id(@Param("t") Trace t, @Param("entity") KhCompany entity);

	List<KhCompany> findByName(@Param("t") Trace t, @Param("name") String name);

}
