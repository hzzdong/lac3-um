package com.linkallcloud.um.activity.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.Company;

public interface ICompanyActivity<T extends Company> extends IOrgActivity<T> {

	List<T> findSubCompanies(Trace t, Long companyId);

	/**
	 * 根据treeType得到company的机构树
	 * 
	 * @param t
	 * @param treeType
	 * @param company
	 * @return
	 */
	Tree getCompanyTree(Trace t, String treeType, Sid company);

	/**
	 * 根据treeType得到company的机构树列表
	 *
	 * @param t
	 * @param treeType
	 * @param companyId
	 * @return
	 */
	List<Tree> getCompanyTreeList(Trace t, String treeType, Sid companyId);

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

	Tree loadCompanyMenuTree(Trace t, Long companyId, Long appId);
	Tree loadCompanyOrgTree(Trace t, Long companyId);

	Long[] findPermedCompanyAppMenus(Trace t, Long companyId, Long appId);

	Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds);

	/**
	 * 得到公司管理员在系统设置中设定的根区域ids。若未设定，返回null。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Long[] getConfigCompanyAreaRootIds(Trace t, Long companyId);
	
	/**
	 * 公司全局根区域的ids。由公司管理员在系统设置中设定。若未设定，默认同父公司。顶层公司未设定为系统全区域。
	 * 
	 * @param t
	 * @param companyId
	 * @return
	 */
	Long[] getCompanyAreaRootIds(Trace t, Long companyId);
	List<NameValue> getConfigCompanyAreaRoots(Trace t, Long companyId);
	
	Boolean updateCompanyLogo(Trace t, Sid companyId, String logo);
	
	Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds);
	Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds);

}
