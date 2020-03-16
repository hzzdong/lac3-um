package com.linkallcloud.um.iapi.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.KhCompany;

public interface IKhCompanyManager extends ICompanyManager<KhCompany> {

    /**
     * 统计镇对应的村个数 (按县、镇管理员登录)
     */
    List<KhCompany> countByArea4id(Trace t, KhCompany entity);

    Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds);

    Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds);

    List<Tree> findPermedKhCompanyAppMenus(Trace t, Long ywCompanyId, Long khCompanyId, Long appId);

}
