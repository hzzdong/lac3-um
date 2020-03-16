package com.linkallcloud.um.iapi.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.ITreeManager;
import com.linkallcloud.um.domain.party.Party;

public interface IPartyManager<T extends Party> extends ITreeManager<T> {

	/**
	 * 根据行政编码查询
	 * 
	 * @param t
	 * @param govCode
	 *            Company:编码；Department：部门编号；User：员工工号
	 * @return
	 */
	T fetchByGovCode(Trace t, String govCode);

}
