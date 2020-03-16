package com.linkallcloud.um.server.dao.party;

import com.linkallcloud.core.dao.ITreeDao;
import com.linkallcloud.um.domain.party.Party;

public interface IPartyDao<T extends Party> extends ITreeDao<T> {
	
//	/**
//	 * 根据行政编码查询
//	 * 
//	 * @param tid
//	 * @param govCode
//	 *            Company:编码；Department：部门编号；User：员工工号
//	 * @return
//	 */
//	T fetchByGovCode(@Param("t") Trace t, @Param("govCode") String govCode);

}
