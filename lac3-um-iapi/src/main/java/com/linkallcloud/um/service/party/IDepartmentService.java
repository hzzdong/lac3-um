package com.linkallcloud.um.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Department;

import java.util.List;

public interface IDepartmentService<T extends Department> extends IOrgService<T> {

	/**
     * 得到公司下所有的部门和子部门
     * 
     * @param t
     * @param companyId
     * @return
     */
    List<T> findCompanyDepartments(Trace t, Long companyId);

    /**
     * 根据companyId获取此单位直接（一级）部门
     * 
     * @param t
     * @param companyId
     * @return
     */
    List<T> findCompanyDirectDepartments(Trace t, Long companyId);
    

    /**
     * 根据parentDepartmentGoveCode获取此部门下所有子部门
     * 
     * @param t
     * @param parentDepartmentCode
     * @return
     */
    List<T> findDepartmentsByParentDepartmentCode(Trace t, String parentDepartmentCode);

    /**
     * 根据parentDepartmentId获取此部门下直接子部门
     * 
     * @param t
     * @param parentDepartmentId
     * @return
     */
    List<T> findDirectDepartmentsByParentDepartmentId(Trace t, Long parentDepartmentId);

}
