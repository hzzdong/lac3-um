package com.linkallcloud.um.activity.party;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.dto.base.PermedAreaVo;

public interface ICompanyActivity<T extends Company> extends IOrgActivity<T> {

	List<T> findSubCompanies(Trace t, Long companyId);

	Long getCompanyAreaRootId(Trace t, Long companyId, Long appId);

	Long getCompanyAreaRootIdBySystemConfig(Trace t, Long companyId);

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
	Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds);

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
	Boolean updateCompanyLogo(Trace t, Sid companyId, String logo);

}
