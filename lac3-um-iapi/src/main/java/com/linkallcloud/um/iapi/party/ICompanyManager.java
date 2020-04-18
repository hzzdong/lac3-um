package com.linkallcloud.um.iapi.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.dto.base.PermedAreaVo;

public interface ICompanyManager<T extends Company> extends IOrgManager<T> {

	/**
	 * 获取直接子公司列表
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<T> findSubCompanies(Trace t, Long companyId);

	/**
	 * 得到companyGovCode及其以下的机构树节点，包括所有子公司节点，所有部门节点。
	 * 
	 * @param t
	 * @param companyGovCode
	 * @return
	 */
	List<Tree> getCompanyFullOrgTreeByGovCode(Trace t, String companyGovCode);

	/**
	 * 得到companyId及其以下的机构树节点，包括所有子公司节点，所有部门节点。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<Tree> getCompanyFullOrgTreeList(Trace t, Long companyId);

	/**
	 * 得到companyId及其以下的机构树节点，包括所有子公司节点，所有部门节点。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Tree getCompanyFullOrgTree(Trace t, Long companyId);

	/**
	 * 得到公司被通过授权等定义后授权应用区域权限的根节点
	 * 
	 * @param t
	 * @param companyId
	 * @param appId
	 * @return
	 */
	Long getCompanyAreaRootId(Trace t, Long companyId, Long appId);

	/**
	 * 得到公司被通过授权等定义出来的区域（root+其下第一级列表）
	 * 
	 * @param t
	 * @param companyId
	 * @param appId
	 * @return
	 */
	List<Tree> getDefinedCompanyAreas(Trace t, Long companyId, Long appId);

	List<T> findDirectCompaniesByParentId(Trace t, Long parentCompanyId);

	List<Tree> getPermedCompanyOrgs(Trace t, Long appId, Long userId);

	/**
	 * 得到某应用的菜单，并根据某公司是否有权限打上标记
	 * 
	 * @param t
	 * @param myCompanyId
	 * @param forCompanyId
	 * @param appId
	 * @return
	 */
	List<Tree> findPermedCompanyAppMenus(Trace t, Long myCompanyId, Long forCompanyId, Long appId);

	Boolean saveCompanyAppMenuPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds);

	PermedAreaVo findPermedCompanyAppAreas(Trace t, Long myCompanyId, Long forCompanyId, Long parentAreaId, Long appId);

	Boolean saveCompanyAppAreaPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> uuidIds);

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

}
