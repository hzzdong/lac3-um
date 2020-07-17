package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.User;

public interface IUserDao<T extends User> extends IPartyDao<T> {

	List<T> find4Company(@Param("t") Trace t, @Param("companyId") Long companyId);

	List<T> find4Department(@Param("t") Trace t, @Param("departmentId") Long departmentId);

	List<T> findByMobile(@Param("t") Trace t, @Param("mobile") String mobile);

	T fecthByAccount(@Param("t") Trace t, @Param("account") String account);

	boolean updateLastLoginTime(@Param("t") Trace t, @Param("id") Long userId);

	List<T> leaderPage(@Param("t") Trace t, @Param("page") Page<T> page);

	List<T> findOrgLeaders(@Param("t") Trace t, @Param("orgId") Long orgId, @Param("orgUuid") String orgUuid);

	/**
	 * 查询某角色已分配的用户
	 *
	 * @param t
	 * @param page 查询条件中必须包含：roleId和roleUuid参数
	 * @return
	 */
	List<T> findPage4Role(@Param("t") Trace t, @Param("page") Page<T> page);

	List<T> findPage4UnRole(@Param("t") Trace t, @Param("page") Page<T> page);

//	/**
//	 * 查询某角色已分配的用户
//	 *
//	 * @param t
//	 * @param roleId
//	 * @return
//	 */
//	List<T> find4RoleById(@Param("t") Trace t, @Param("roleId") Long roleId);
//
//	/**
//	 * 查询某角色已分配的用户
//	 *
//	 * @param t
//	 * @param roleIds
//	 * @return
//	 */
//	List<T> find4RoleByIds(@Param("t") Trace t, @Param("roleIds") Long[] roleIds);
//
//	/**
//	 * 查询某角色已分配的用户
//	 *
//	 * @param t
//	 * @param roleGovCode
//	 * @return
//	 */
//	List<T> find4RoleByGovCode(@Param("t") Trace t, @Param("roleGovCode") String roleGovCode);
//
//	/**
//	 * 查询某角色已分配的用户
//	 *
//	 * @param t
//	 * @param roleGovCodes
//	 * @return
//	 */
//	List<T> find4RoleByGovCodes(@Param("t") Trace t, @Param("roleGovCodes") String[] roleGovCodes);

//	List<T> findByRoleCompany(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("roleId") Long roleId);
//
//	/**
//	 * 查询某角色已分配给某部门的用户
//	 *
//	 * @param t
//	 * @param departmentId
//	 * @param roleId
//	 * @return
//	 */
//	List<T> findDepartmentUser4RoleById(@Param("t") Trace t, @Param("departmentId") Long departmentId,
//			@Param("roleId") Long roleId);
//
//	/**
//	 * 查询某角色已分配给某部门的用户
//	 *
//	 * @param t
//	 * @param departmentId
//	 * @param roleIds
//	 * @return
//	 */
//	List<T> findDepartmentUser4RoleByIds(@Param("t") Trace t, @Param("departmentId") Long departmentId,
//			@Param("roleIds") Long[] roleIds);
//
//	/**
//	 * 查询某角色已分配给某部门的用户
//	 *
//	 * @param t
//	 * @param departmentId
//	 * @param roleGovCode
//	 * @return
//	 */
//	List<T> findDepartmentUser4RoleByGovCode(@Param("t") Trace t, @Param("departmentId") Long departmentId,
//			@Param("roleGovCode") String roleGovCode);
//
//	/**
//	 * 查询某角色已分配给某部门的用户
//	 *
//	 * @param t
//	 * @param departmentId
//	 * @param roleGovCodes
//	 * @return
//	 */
//	List<T> findDepartmentUser4RoleByGovCodes(@Param("t") Trace t, @Param("departmentId") Long departmentId,
//			@Param("roleGovCodes") String[] roleGovCodes);

	/**
	 * 添加用户角色
	 *
	 * @param t
	 * @param userId
	 * @param roleIds
	 * @param companyId 授权公司id
	 * @return
	 */
	boolean addUserRoles(@Param("t") Trace t, @Param("userId") Long userId, @Param("roleIds") List<Long> roleIds,
			@Param("companyId") Long companyId);

	/**
	 * 移除用户角色
	 *
	 * @param t
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	boolean removeUserRoles(@Param("t") Trace t, @Param("userId") Long userId, @Param("roleIds") List<Long> roleIds,
			@Param("companyId") Long companyId);

	/**
	 * 移除用户所有角色
	 *
	 * @param t
	 * @param userId
	 * @return
	 */
	boolean removeUserAllRoles(@Param("t") Trace t, @Param("userId") Long userId, @Param("companyId") Long companyId);

	/**
	 * 根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
	 *
	 * @param t
	 * @param page
	 * @return
	 */
	List<T> findPermedUserPage4Select(@Param("t") Trace t, @Param("page") Page<T> page);

	/**
	 * 根据政务服务网uid查询用户
	 *
	 * @param t
	 * @param zwuid
	 * @return
	 */
	T fecthByAZwuid(@Param("t") Trace t, @Param("zwuid") String zwuid);

	/**
	 * 根据钉钉uid查询用户
	 *
	 * @param t
	 * @param dduid
	 * @return
	 */
	T fecthByDduid(@Param("t") Trace t, @Param("dduid") String dduid);

	/**
	 * 更新用户的政务服务网uid
	 *
	 * @param t
	 * @param umUserId
	 * @param zwuid
	 * @return
	 */
	int updateZwuid(@Param("t") Trace t, @Param("umUserId") Long umUserId, @Param("zwuid") String zwuid);

	/**
	 * 更新用户的钉钉uid
	 *
	 * @param t
	 * @param umUserId
	 * @param dduid
	 * @param ddStatus
	 * @return
	 */
	int updateDduid(@Param("t") Trace t, @Param("umUserId") Long umUserId, @Param("dduid") String dduid,
			@Param("ddStatus") int ddStatus);

	/**
	 * 更新单位下所有用户的状态
	 *
	 * @param t
	 * @param status
	 * @param companyId
	 * @return
	 */
	int updateStatusByCompany(@Param("t") Trace t, @Param("status") int status, @Param("companyId") Long companyId);

	/**
	 * 更新部门下所有用户的状态
	 *
	 * @param t
	 * @param status
	 * @param departmentId
	 * @return
	 */
	int updateStatusByDepartment(@Param("t") Trace t, @Param("status") int status,
			@Param("departmentId") Long departmentId);

	List<T> findCompanyAdmin(@Param("t") Trace t, @Param("company") Sid company);

	int updateHeaderImage(@Param("t") Trace t, @Param("user") Sid user, @Param("ico") String ico);

	/**
	 * 客户单位操作员，查公司或者部门下人员分页列表
	 * 
	 * @param t
	 * @param page
	 * @return
	 */
	List<T> findUserPage4Org(@Param("t") Trace t, @Param("page") Page<T> page);

	/**
	 * 客户单位操作员，根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
	 * 
	 * @param t
	 * @param page
	 * @return
	 */
	List<T> findPermedUserPage(@Param("t") Trace t, @Param("page") Page<T> page);

	/**
	 * 获取某用户某App的组织权限，不区分superadmin和管理员，都按照表中权限数据查询。
	 *
	 * @param t
	 * @param userId
	 * @param appId
	 * @return
	 */
	List<Long> findUserAppOrgs(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("userId") Long userId,
			@Param("appId") Long appId);

	List<Long> findUserAppAreas(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("userId") Long userId,
			@Param("appId") Long appId);

	String[] findUserAppMenuResCodes(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("userId") Long userId, @Param("appId") Long appId);

}
