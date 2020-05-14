package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.service.ITreeService;
import com.linkallcloud.um.domain.sys.Menu;

import java.util.List;

public interface IMenuService extends ITreeService<Menu> {

    List<Menu> findAppMenus(Trace t, Long appId, boolean valid);

    /**
     * 获取应用的菜单树
     * 
     * @param t
     * @param appId
     * @param status
     * @return
     */
    Tree getMenuTree(Trace t, Sid appId, Boolean status);

    /**
     * 获取应用的菜单
     * 
     * @param t
     * @param appId
     * @param status
     * @return
     */
    List<Tree> getMenus(Trace t, Sid appId, Boolean status);

    /**
     * 获取应用有效状态的菜单
     *
     * @param t
     * @param appCode
     * @return
     */
    List<Tree> getValidMenus(Trace t, String appCode);
    List<Menu> getValidMenuList(Trace t, String appCode);
    /**
     * 获取应用有效状态的菜单
     * 
     * @param t
     * @param appId
     * @return
     */
    List<Tree> getValidMenus(Trace t, Long appId);
    List<Tree> getValidMenusWithButton(Trace t, Long appId);

    // List<Tree> findKhCompanyValidMenus(Trace t, Long companyId, Long appId);

    // List<Tree> findYwCompanyValidMenus(Trace t, Long ywCompanyId, Long appId, String appUuid);

}
