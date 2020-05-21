package com.linkallcloud.um.iapi.sys;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.ITreeManager;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.um.domain.sys.Area;

public interface IAreaManager extends ITreeManager<Area> {

	Area fetchByGovCode(Trace t, String areaRootGovCode);

	/**
	 * 统计子节点数量(非删除状态)
	 * 
	 * @param t
	 * @param parenId
	 * @param statusRule
	 * @return
	 */
	int getChildrenCount(Trace t, Long parenId, QueryRule statusRule);

	/**
	 * 返回parentCode节点及其下的所有子节点(非删除状态)
	 * 
	 * @param t
	 * @param parentCode
	 * @param statusRule
	 * @return
	 */
	List<Area> findChildren(Trace t, String parentCode, QueryRule statusRule);

	/**
	 * 返回parentId节点下的直接子节点(非删除状态)
	 * 
	 * @param t
	 * @param parentId
	 * @param statusRule
	 * @return
	 */
	List<Area> findDirectChildren(Trace t, Long parentId, QueryRule statusRule);

	/**
	 * 返回parentCode节点及其下的所有子节点(非删除状态)
	 * 
	 * @param t
	 * @param parentCode
	 * @param levelLt    level小于
	 * @return
	 */
	List<Area> findByParentCodeAndLevel(Trace t, String parentCode, int levelLt);

	List<Area> findPermedKhCompanyAppAreas(Trace t, Long khCompanyId, Long appId);

	List<Area> findPermedYwCompanyAppAreas(Trace t, Long ywCompanyId, Long appId);

}
