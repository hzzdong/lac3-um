package com.linkallcloud.um.iapi.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.dto.base.PermedAreaVo;

import java.util.List;
import java.util.Map;

public abstract interface IRoleManager<T extends Role, U extends User> extends IPartyManager<T> {

    /**
     * 根据companyId，获取companyId所在公司的角色page
     *
     * @param t
     * @param companyId
     * @param type      type=Domains.ROLE_NORMAL:本公司普通角色；type=Domains.ROLE_SYSTEM:本公司系统角色；其它：本公司全部角色
     * @param page
     * @return
     */
    Page<T> findCompanyRolePage(Trace t, Long companyId, int type, Page<T> page)
            throws BaseRuntimeException;

    /**
     * 根据companyId，获取companyId所在公司的角色
     *
     * @param t
     * @param companyId
     * @param type      type=Domains.ROLE_NORMAL:本公司普通角色；type=Domains.ROLE_SYSTEM:本公司系统角色；其它：本公司全部角色
     * @return
     */
    List<T> findCompanyRoles(Trace t, Long companyId, int type) throws BaseRuntimeException;

    /**
     * 根据orgId，获取orgId所在公司的所有角色page，包含系统角色
     *
     * @param t
     * @param companyId
     * @param page
     * @return
     * @throws BaseRuntimeException
     */
    Page<T> findCompanyAllRolePage(Trace t, Long companyId, Page<T> page) throws BaseRuntimeException;

    /**
     * 根据orgId，获取orgId所在公司的所有角色，包含系统角色
     *
     * @param t
     * @param companyId
     * @return
     * @throws BaseRuntimeException
     */
    List<T> findCompanyAllRoles(Trace t, Long companyId) throws BaseRuntimeException;

    /**
     * 根据orgId和levelNotEqual，获取orgId所在公司的所有角色，包含level符合的系统角色
     *
     * @param t
     * @param companyId
     * @param roleLevel roleLevel=9:所有角色，roleLevel!=9:增加条件level<>9
     * @return
     * @throws BaseRuntimeException
     */
    List<T> findCompanyRolesByLevel(Trace t, Long companyId, Integer roleLevel) throws BaseRuntimeException;

    /**
     * 获取用户拥有的角色分页列表
     *
     * @param t
     * @param page
     * @return
     */
    Page<T> findPage4User(Trace t, Page<T> page);

    /**
     * 获取用户未拥有的角色分页列表
     *
     * @param t
     * @param page
     * @return
     */
    Page<T> findNoRolePage4User(Trace t, Page<T> page);

    List<T> find4User(Trace t, Long userId);

    /**
     * 给角色分配人员
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param userUuidIds
     * @return
     */
    boolean addRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds);

    /**
     * 删除角色已分配的人员
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param userUuidIds
     * @return
     */
    boolean removeRoleUsers(Trace t, Long roleId, String roleUuid, Map<String, Long> userUuidIds);

    boolean clearRoleUsers(Trace t, Long roleId, String roleUuid);

    List<U> getRoleUsers(Trace t, Long roleId, String roleUuid);

    /**
     * 增加角色许可访问的应用
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param appUuidIds
     * @return
     */
    boolean addRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds);

    /**
     * 删除角色许可访问的应用
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param appUuidIds
     * @return
     */
    boolean removeRoleApps(Trace t, Long roleId, String roleUuid, Map<String, Long> appUuidIds);

    /**
     * 保存某角色某应用的菜单权限
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param appId
     * @param appUuid
     * @param menuUuidIds
     * @return
     */
    Boolean saveRoleAppMenuPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
                                Map<String, Long> menuUuidIds);

    /**
     * 保存某角色某应用的组织权限
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param appId
     * @param appUuid
     * @param menuUuidIds
     * @return
     */
    Boolean saveRoleAppOrgPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
                               Map<String, Long> menuUuidIds);

    /**
     * 保存某角色某应用区域权限
     *
     * @param t
     * @param roleId
     * @param roleUuid
     * @param appId
     * @param appUuid
     * @param menuUuidIds
     * @return
     */
    Boolean saveRoleAppAreaPerm(Trace t, Long roleId, String roleUuid, Long appId, String appUuid,
                                Map<String, Long> menuUuidIds);

    /**
     * 得到某应用的菜单，并根据某角色是否有权限打上标记
     *
     * @param t
     * @param companyId
     * @param roleId
     * @param appId
     * @return
     */
    List<Tree> findPermedMenus(Trace t, Long companyId, Long roleId, Long appId);

    /**
     * 得到某应用的组织权限，并根据某角色是否有权限打上标记
     *
     * @param t
     * @param companyId
     * @param roleId
     * @param appId
     * @return
     */
    List<Tree> findPermedOrgs(Trace t, Long companyId, Long roleId, Long appId);

    /**
     * 得到某应用的区域权限，并根据某角色是否有权限打上标记（适合前台调用）
     *
     * @param t
     * @param parentAreaId
     * @param companyId
     * @param roleId
     * @param appId
     * @return
     */
    PermedAreaVo findPermedRoleAppAreas(Trace t, Long parentAreaId, Long companyId, Long roleId, Long appId);

    //Long getRoleAreaRootId(Trace t, Long roleId, Long appId);

    /**
     * 得到某公司区域的根，用于给某角色某应用设置区域权限（适合后台调用）
     *
     * @param t
     * @param companyId
     * @return
     */
    Long getCompanyAreaRootId4Role(Trace t, Long companyId);

//	/**
//	 * 得到某角色某应用可分配的区域资源（适合后台调用）
//	 * 
//	 * @param t
//	 * @param roleId
//	 * @param appId
//	 * @return
//	 */
//	PermedAreaVo findRoleValidAreaResource(Trace t, Long roleId, Long appId);

    /**
     * 得到某公司可分配的区域资源，用于给某角色某应用设置区域权限（适合后台调用）
     *
     * @param t
     * @param companyId
     * @return
     */
    PermedAreaVo findCompanyValidAreaResource4Role(Trace t, Long companyId);

    /**
     * 根据用户id，得到此用户拥有的所在公司的role列表
     *
     * @param t
     * @param userId
     * @return
     */
    List<T> findCompanyRoles4Me(Trace t, long userId);
}
