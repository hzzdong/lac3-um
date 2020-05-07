package com.linkallcloud.um.iapi.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.web.session.SessionUser;

public abstract interface IUserManager<T extends User> extends IPartyManager<T> {

	List<T> find4Company(Trace t, Long companyId);

	List<T> find4Department(Trace t, Long departmentId);

	List<T> findByMobile(Trace t, String mobile);

	T fecthByAccount(Trace t, String account);

	T loginValidate(Trace t, String accountOrMobile, String password) throws BaseRuntimeException;

	boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd)
			throws BaseRuntimeException;

	boolean addUserRoles(Trace t, Long userId, String userUuid, Map<String, Long> roleUuidIds);

	boolean removeUserRoles(Trace t, Long userId, String userUuid, Map<String, Long> roleUuidIds);

	/**
	 * 返回某用户某角色具有权限的菜单资源的resCode数组
	 * 
	 * @param t
	 * @param userId
	 * @param appId
	 * @return
	 */
	String[] getUserAppPermissions4Menu(Trace t, Long userId, Long appId);

	void cleanCache4UserAppPermissions4Menu(Trace t, Long userId, Long appId);

	/**
	 * 获取某用户某应用的菜单
	 * 
	 * @param t
	 * @param userId
	 * @param appId
	 * @return
	 */
	Tree getUserAppMenu(Trace t, Long userId, Long appId);

	void cleanCache4UserAppMenu(Trace t, Long userId, Long appId);

	/**
	 * 查询某角色已分配的用户
	 * 
	 * @param t
	 * @param page 查询条件中必须包含：roleId和roleUuid参数
	 * 
	 * @return
	 */
	Page<T> findPage4Role(Trace t, Page<T> page);

	/**
	 * 查询某角色已分配的用户
	 * 
	 * @param t
	 * @param roleId
	 * @return
	 */
	List<T> find4Role(Trace t, Long roleId);

	/**
	 * 查询某角色已分配的用户
	 * 
	 * @param t
	 * @param roleIds
	 * @return
	 */
	List<T> find4Role(Trace t, Long[] roleIds);

	/**
	 * 查询某角色已分配的用户
	 * 
	 * @param t
	 * @param roleGovCode
	 * @return
	 */
	List<T> find4Role(Trace t, String roleGovCode);

	/**
	 * 查询某角色已分配的用户
	 * 
	 * @param t
	 * @param roleGovCodes
	 * @return
	 */
	List<T> find4Role(Trace t, String[] roleGovCodes);

	List<T> findByRoleCompany(Trace t, Long companyId, Long roleId);

	/**
	 * 查询某角色已分配给某部门的用户
	 * 
	 * @param t
	 * @param departmentId
	 * @param roleId
	 * @return
	 */
	List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long roleId);

	/**
	 * 查询某角色已分配给某部门的用户
	 * 
	 * @param t
	 * @param departmentId
	 * @param roleIds
	 * @return
	 */
	List<T> findDepartmentUser4Role(Trace t, Long departmentId, Long[] roleIds);

	/**
	 * 查询某角色已分配给某部门的用户
	 * 
	 * @param t
	 * @param roleGovCode
	 * @return
	 */
	List<T> findDepartmentUser4Role(Trace t, Long departmentId, String roleGovCode);

	/**
	 * 查询某角色已分配给某部门的用户
	 * 
	 * @param t
	 * @param departmentId
	 * @param roleGovCodes
	 * @return
	 */
	List<T> findDepartmentUser4Role(Trace t, Long departmentId, String[] roleGovCodes);

	/**
	 * 根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
	 * 
	 * @param t
	 * @param page
	 * @return
	 */
	Page<T> findPermedUserPage4Select(Trace t, Page<T> page);

	/**
	 * 根据政务服务网uid查询用户
	 * 
	 * @param t
	 * @param zwuid
	 * @return
	 */
	T fecthByAZwuid(Trace t, String zwuid);

	/**
	 * 根据钉钉uid查询用户
	 * 
	 * @param t
	 * @param dduid
	 * @return
	 */
	T fecthByDduid(Trace t, String dduid);

	/**
	 * 更新用户的政务服务网uid
	 * 
	 * @param t
	 * @param umUserId
	 * @param zwuid
	 * @return
	 */
	boolean updateZwuid(Trace t, Long umUserId, String zwuid);

	/**
	 * 更新用户的钉钉uid
	 * 
	 * @param t
	 * @param umUserId
	 * @param dduid
	 * @param ddStatus
	 * @return
	 */
	boolean updateDduid(Trace t, Long umUserId, String dduid, int ddStatus);

	SessionUser assembleSessionUser(Trace t, String loginName, String appCode);

	T fetchCompanyAdmin(Trace t, Sid companyId);

}
