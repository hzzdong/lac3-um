package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.Company;

public interface ICompanyDao<T extends Company> extends IOrgDao<T> {

	int updateAreaFields(@Param("t") Trace t, @Param("entity") T entity);

	/**
	 * 根据父公司的ID，查找直接子公司列表
	 * 
	 * @param t
	 * @param parentCompanyId
	 * @return
	 */
	List<T> findByParent(@Param("t") Trace t, @Param("parentCompanyId") Long parentCompanyId);

	/**
	 * 根据父公司的govCode，查找所有子公司列表
	 * 
	 * @param t
	 * @param parentCode
	 * @param parentCodeLen
	 * @return
	 */
	List<T> findAllCompaniesByParentCode(@Param("t") Trace t, @Param("parentCode") String parentCode,
			@Param("len") int parentCodeLen);

	Long[] findPermedCompanyAppAreas(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("appId") Long appId);

	void clearCompanyAppAreaPerms(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("appId") Long appId);

	void saveCompanyAppAreaPerms(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("appId") Long appId,
			@Param("areaIds") List<Long> areaIds);

	Long[] findPermedCompanyAppMenus(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("appId") Long appId);

	void clearCompanyAppMenuPerms(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("appId") Long appId);

	void saveCompanyAppMenuPerms(@Param("t") Trace t, @Param("companyId") Long companyId, @Param("appId") Long appId,
			@Param("menuIds") List<Long> menuIds);
	
	int updateCompanyLogo(@Param("t") Trace t, @Param("id") Long id, @Param("logo") String logo);
	
	int addApps(@Param("t") Trace t, @Param("id") Long id, @Param("appIds") List<Long> appIds);
	int removeApps(@Param("t") Trace t, @Param("id") Long id, @Param("appIds") List<Long> appIds);

}
