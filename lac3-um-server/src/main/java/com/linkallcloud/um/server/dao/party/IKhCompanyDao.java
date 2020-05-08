package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.KhCompany;

public interface IKhCompanyDao extends ICompanyDao<KhCompany> {

    List<KhCompany> findNoBonusPointsAccountVillages(Trace t);

    /**
     * 统计镇对应的村个数 (按县、镇管理员登录)
     */
    List<KhCompany> countByArea4id(Trace t, KhCompany entity);

    List<KhCompany> findByName(@Param("t") Trace t, @Param("name") String name);

    Boolean addApps(@Param("t") Trace t, @Param("id") Long id, @Param("appIds") List<Long> appIds);

    Boolean removeApps(@Param("t") Trace t, @Param("id") Long id, @Param("appIds") List<Long> appIds);

	int updateCompanyLogo(@Param("t") Trace t, @Param("id") Long id, @Param("logo") String logo);

}
