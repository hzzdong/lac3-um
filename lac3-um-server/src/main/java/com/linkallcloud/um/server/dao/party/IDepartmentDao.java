package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Department;

public interface IDepartmentDao<T extends Department> extends IOrgDao<T> {

	/**
	 * 查找companyId下的所有部门，不包含子公司下的部门
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<T> findCompanyDepartments(@Param("t") Trace t, @Param("companyId") Long companyId);

	/**
	 * 查找companyIds下的所有部门，包含子公司下的部门
	 * 
	 * @param t
	 * @param   companyIds，若为null，这查找所有部门
	 * @return
	 */
	List<T> findAllDepartments(@Param("t") Trace t, @Param("companyIds") List<Long> companyIds);
	
	/**
     * 根据companyGoveCode获取此单位直接（一级）部门
     * 
     * @param t
     * @param id
     * @return
     */
    List<T> findCompanyDirectDepartments(@Param("t") Trace t, @Param("companyId") Long companyId);

    /**
     * 根据parentDepartmentCode获取此部门下所有子部门
     * 
     * @param t
     * @param parentDepartmentCode
     * @return
     */
    List<T> findDepartmentsByParentDepartmentCode(@Param("t") Trace t,
            @Param("parentDepartmentCode") String parentDepartmentCode);

    /**
     * 根据parentDepartmentGoveCode获取此部门下直接子部门
     * 
     * @param t
     * @param parentDepartmentId
     * @return
     */
    List<T> findDirectDepartmentsByParentDepartmentId(@Param("t") Trace t,
            @Param("parentDepartmentId") Long parentDepartmentId);

}
