package com.linkallcloud.um.activity.party;

import com.linkallcloud.core.activity.ITreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Party;

public interface IPartyActivity<T extends Party> extends ITreeActivity<T> {

    /**
     * 根据行政编码查询
     *
     * @param t
     * @param govCode Company:编码；Department：部门编号；User：员工工号
     * @return
     */
    T fetchByGovCode(Trace t, String govCode);

}
