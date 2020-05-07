package com.linkallcloud.um.service.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.dto.base.PermedAreaVo;

public interface ICompanyService<T extends Company> extends IOrgService<T> {

	List<T> findSubCompanies(Trace t, Long companyId);

	Long getCompanyAreaRootId(Trace t, Long companyId, Long appId);

	Long getCompanyAreaRootIdBySystemConfig(Trace t, Long companyId);

	/**
	 * 根据父公司的ID，查找直接子公司列表
	 * 
	 * @param t
	 * @param parentId
	 * @return
	 */
	List<T> findDirectCompaniesByParentId(Trace t, Long parentId);

	/**
	 * 根据父公司的govCode，查找所有子公司列表
	 * 
	 * @param t
	 * @param govCode
	 * @return
	 */
	List<T> findAllCompaniesByParentCode(Trace t, String govCode);

	Tree findCompanyValidMenuTree(Trace t, Long companyId, Long appId);

	List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId);

	List<Tree> findCompanyValidOrgResource(Trace t, Long companyId);

	PermedAreaVo findCompanyValidAreaResource(Trace t, Long companyId, Long appId);

	Long[] findPermedCompanyAppAreas(Trace t, Long companyId, Long appId);

	Boolean saveCompanyAppAreaPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> areaUuidIds);

	Long[] findPermedCompanyAppMenus(Trace t, Long companyId, Long appId);

	Boolean saveCompanyAppMenuPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds);

	/*
	 * -----------------------------------------------------------------------------
	 */

	/**
	 * 公司某应用的根区域ids
	 * 
	 * @param t
	 * @param companyId
	 * @param appId
	 * @return
	 */
	Long[] getCompanyAppAreaRootIds(Trace t, Long companyId, Long appId);

	/**
	 * 得到公司管理员在系统设置中设定的根区域ids。若未设定，返回null。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Long[] getConfigCompanyAreaRootIds(Trace t, Sid companyId);

	List<NameValue> getConfigCompanyAreaRoots(Trace t, Sid companyId);

	/**
	 * 公司全局根区域的ids。由公司管理员在系统设置中设定。若未设定，默认同父公司。顶层公司未设定为系统全区域。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Long[] getCompanyAreaRootIds(Trace t, Sid companyId);

	/**
	 * 公司全局可设置根区域树。1.顶层公司为系统全区域；2.子公司为父公司根区域（父公司管理员给父公司设定的根区域，若未设置，继续往上找）；
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Tree loadCompanyAreaFullTree(Trace t, Sid companyId);

	/**
	 * 公司管理员在系统设置中设定的公司区域树。 若未设定，默认同父公司。顶层公司未设定为系统全区域。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Tree loadCompanyAreaTree(Trace t, Sid companyId);

	Tree findPermedAppMenusTree(Trace t, Sid myCompanyId, Sid forCompanyId, Sid appId);

	Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds);

	/**
	 * 得到某用户某应用的机构权限树
	 * 
	 * @param t
	 * @param appId
	 * @param userId
	 * @return
	 */
	Tree getPermedCompanyTree(Trace t, Long appId, Long userId);

	/**
	 * 根据treeType得到companyId的机构树
	 * 
	 * @param t
	 * @param treeType,参见com.linkallcloud.um.constant.Consts
	 * @param companyId
	 * @return
	 */
	Tree getCompanyTree(Trace t, String treeType, Sid companyId);

}
