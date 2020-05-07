package com.linkallcloud.um.iapi.party;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;

public interface IDepartmentManager<T extends Department, U extends User> extends IOrgManager<T, U> {

	/**
	 * 得到公司下所有的部门和子部门
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<T> findCompanyDepartments(Trace t, Long companyId);

	/**
	 * 根据companyGoveCode获取此政府单位（虚拟节点"部门"下）的实际部门和子部门
	 * 
	 * @param t
	 * @param companyGoveCode (1)系统中注册的companyGoveCode是用company所在区域的GovCode。
	 *                        (2)政府组织机构注册的时候company下面会注册虚拟节点"部门"，其GovCode为companyGoveCode+"-bm"。
	 * @return
	 */
	List<T> findZfDepartmentsByCompanyGovCode(Trace t, String companyGoveCode);

	/**
	 * 根据companyGoveCode获取此政府单位（虚拟节点"部门"下）的实际一级部门（不包含子部门）
	 * 
	 * @param t
	 * @param companyGoveCode (1)系统中注册的companyGoveCode是用company所在区域的GovCode。
	 *                        (2)政府组织机构注册的时候company下面会注册虚拟节点"部门"，其GovCode为companyGoveCode+"-bm"。
	 * @return
	 */
	List<T> findZfTopDepartmentsByCompanyGovCode(Trace t, String companyGoveCode);

	/**
	 * 根据companyId获取此单位直接（一级）部门
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<T> findCompanyDirectDepartments(Trace t, Long companyId);

	/**
	 * 根据parentDepartmentId获取此部门下所有子部门
	 * 
	 * @param t
	 * @param parentDepartmentId
	 * @return
	 */
	List<T> findDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId);

	/**
	 * 根据parentDepartmentGoveCode获取此部门下直接子部门
	 * 
	 * @param t
	 * @param parentDepartmentGoveCode
	 * @return
	 */
	List<T> findDirectDepartmentsByParentDepartmentGovCode(Trace t, String parentDepartmentGoveCode);

	/**
	 * 根据parentDepartmentId获取此部门下直接子部门
	 * 
	 * @param t
	 * @param parentDepartmentId
	 * @return
	 */
	List<T> findDirectDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId);
}
