package com.linkallcloud.um.iapi.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.User;

public interface ICompanyManager<T extends Company, U extends User> extends IOrgManager<T, U> {

	/**
	 * 获取直接子公司列表
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	List<T> findSubCompanies(Trace t, Long companyId);

	List<T> findDirectCompaniesByParentId(Trace t, Long parentCompanyId);

	/**
	 * 得到某应用的菜单，并根据某公司是否有权限打上标记
	 * 
	 * @param t
	 * @param myCompanyId
	 * @param forCompanyId
	 * @param appId
	 * @return
	 */
	Tree findPermedAppMenusTree(Trace t, Sid myCompanyId, Sid forCompanyId, Sid appId);

	Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds);

	/**
	 * 根据权限得到userId所在公司appId应用的机构权限树
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
	

	/**
	 * 公司全局可设置根区域树。1.顶层公司为系统全区域；2.子公司为父公司根区域（父公司管理员给父公司设定的根区域，若未设置，继续往上找）；
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Tree loadCompanyAreaFullTree(Trace t, Sid companyId);
	
	/**
	 * 得到公司管理员在系统设置中设定的根区域ids。若未设定，返回null。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Long[] getConfigCompanyAreaRootIds(Trace t, Long companyId);

	List<NameValue> getConfigCompanyAreaRoots(Trace t, Long companyId);

	/**
	 * 公司全局根区域的ids。由公司管理员在系统设置中设定。若未设定，默认同父公司。顶层公司未设定为系统全区域。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Long[] getCompanyAreaRootIds(Trace t, Long companyId);

	/**
	 * 公司管理员在系统设置中设定的公司区域树。 若未设定，默认同父公司。顶层公司未设定为系统全区域。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Tree loadCompanyAreaTree(Trace t, Sid companyId);

	Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds);
	Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds);

}
