package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.ITreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.sys.Menu;

import java.util.List;

public interface IMenuActivity extends ITreeActivity<Menu> {

    /**
     * 获取应用的菜单列表
     *
     * @param t
     * @param appId
     * @param valid
     * @return
     */
    List<Menu> findAppMenus(Trace t, Long appId, boolean valid);

    /**
     * 获取应用的菜单树
     *
     * @param t
     * @param appCode
     * @return
     */
    Tree getMenuTree(Trace t, String appCode);

    /**
     * 获取应用的菜单
     *
     * @param t
     * @param appId
     * @return
     */
    List<Tree> getMenus(Trace t, Long appId);

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
