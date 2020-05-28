package com.linkallcloud.um.server.dao.party;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.Role;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Menu;

public interface IRoleDao<T extends Role, U extends User> extends IPartyDao<T> {

	/**
	 * 根据companyId，获取companyId所在公司的所有角色，包含系统角色
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<T> findCompanyAllRole(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("cnds") Map<String, Object> cnds);

	/**
	 * 根据orgId和levelNotEqual，获取companyId所在公司的所有角色，包含level符合的系统角色
	 * 
	 * @param t
	 * @param companyId
	 * @param roleLevel roleLevel=9:所有角色，roleLevel!=9:增加条件level<>9
	 * @return
	 */
	List<T> findCompanyRolesByLevel(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("roleLevel") Integer roleLevel);

	/**
	 * 获取用户拥有的角色分页列表
	 * 
	 * @param tid
	 * @param page
	 * @return
	 */
	List<T> findPage4User(@Param("t") Trace t, @Param("page") Page<T> page);

	/**
	 * 获取用户未拥有的角色分页列表
	 * 
	 * @param tid
	 * @param page
	 * @return
	 */
	List<T> findNoRolePage4User(@Param("t") Trace t, @Param("page") Page<T> page);

	/**
	 * 获取用户的角色列表
	 * 
	 * @param tid
	 * @param userId
	 * @return
	 */
	List<T> find4User(@Param("t") Trace t, @Param("userId") Long userId);

	List<U> getRoleUsers(Trace t, Long roleId);

	/**
	 * 给角色分配人员
	 * 
	 * @param tid
	 * @param roleId
	 * @param userIds
	 * @return
	 */
	boolean addRoleUsers(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

	/**
	 * 删除角色已经分配的人员
	 * 
	 * @param tid
	 * @param roleId
	 * @param userIds
	 * @return
	 */
	boolean removeRoleUsers(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

	boolean clearRoleUsers(Trace t, Long roleId);

	/**
	 * 增加角色许可应用
	 * 
	 * @param tid
	 * @param roleId
	 * @param appIds
	 * @return
	 */
	boolean addRoleApps(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appIds") List<Long> appIds);

	/**
	 * 移除角色许可应用
	 * 
	 * @param tid
	 * @param roleId
	 * @param appIds
	 * @return
	 */
	boolean removeRoleApps(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appIds") List<Long> appIds);

	/**
	 * 清空某角色某应用的菜单权限
	 * 
	 * @param tid
	 * @param roleId
	 * @param appId
	 * @return
	 */
	boolean clearRoleAppMenuPerms(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	/**
	 * 清空某角色某应用的菜单权限
	 * 
	 * @param tid
	 * @param roleId
	 * @param appId
	 * @param menuIds
	 * @return
	 */
	boolean saveRoleAppMenuPerms(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId,
			@Param("menuIds") List<Long> menuIds);

	void clearRoleAppOrgPerms(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	void saveRoleAppOrgPerms(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId,
			@Param("orgIds") List<Long> orgIds);

	void clearRoleAppAreaPerms(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	void saveRoleAppAreaPerms(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId,
			@Param("areaIds") List<Long> areaIds);

	/**
	 * 获取某角色某应用有权限的菜单id数组
	 * 
	 * @param tid
	 * @param roleId
	 * @param appId
	 * @return
	 */
	Long[] findPermedMenuIds(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	Long[] findPermedOrgIds(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	Long[] findPermedAreaIds(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	/**
	 * 获取某角色某应用有权限的菜单ResCode数组
	 * 
	 * @param tid
	 * @param roleId
	 * @param appId
	 * @return
	 */
	String[] findRoleAppMenuResCodes(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

	/**
	 * 获取某角色某应用有权限的菜单
	 * 
	 * @param tid
	 * @param roleId
	 * @param appId
	 * @return
	 */
	List<Menu> findRoleAppMenus(@Param("t") Trace t, @Param("roleId") Long roleId, @Param("appId") Long appId);

}
