package com.linkallcloud.um.server.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.ITreeDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Menu;

public interface IMenuDao extends ITreeDao<Menu> {

    /**
     * 获取应用的菜单
     * 
     * @param tid
     * @param appId
     * @param valid
     *            true:status=0的菜单；false:所有菜单
     * @return
     */
    List<Menu> findAppMenus(@Param("t") Trace t, @Param("appId") Long appId, @Param("valid") boolean valid);
    List<Menu> findAppMenusWithButton(@Param("t") Trace t, @Param("appId") Long appId, @Param("valid") boolean valid);

    List<Menu> findKhCompanyAppMenus(@Param("t") Trace t, @Param("khCompanyId") Long khCompanyId,
            @Param("appId") Long appId, @Param("valid") boolean valid);
    List<Menu> findKhCompanyAppMenusWithButton(@Param("t") Trace t, @Param("khCompanyId") Long khCompanyId,
            @Param("appId") Long appId, @Param("valid") boolean valid);

    List<Menu> findYwCompanyAppMenusWithButton(@Param("t") Trace t, @Param("ywCompanyId") Long ywCompanyId,
            @Param("appId") Long appId, @Param("valid") boolean valid);
    List<Menu> findYwCompanyAppMenus(@Param("t") Trace t, @Param("ywCompanyId") Long ywCompanyId,
            @Param("appId") Long appId, @Param("valid") boolean valid);

}
