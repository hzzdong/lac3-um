package com.linkallcloud.um.server.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.ITreeDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.um.domain.sys.Area;

public interface IAreaDao extends ITreeDao<Area> {

	/**
	 * 统计非删除状态的子节点数量
	 * 
	 * @param t
	 * @param parenId
	 * @param statusRule
	 * @return
	 */
	int getChildrenCount(@Param("t") Trace t, @Param("parenId") Long parenId, @Param("sr") QueryRule statusRule);

	/**
	 * 返回parentCode节点及其下的所有子节点
	 * 
	 * @param t
	 * @param parentCode
	 * @param parentCodeLength
	 * @param statusRule
	 * @return
	 */
	List<Area> findByParentCode(@Param("t") Trace t, @Param("parentCode") String parentCode,
			@Param("len") int parentCodeLength, @Param("sr") QueryRule statusRule);

	/**
	 * 返回parentId节点下的直接子节点
	 * 
	 * @param t
	 * @param parentId
	 * @param statusRule
	 * @return
	 */
	List<Area> findByParent(@Param("t") Trace t, @Param("parentId") Long parentId, @Param("sr") QueryRule statusRule);

	List<Area> findPermedKhCompanyAppAreas(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("appId") Long appId);

	List<Area> findPermedYwCompanyAppAreas(@Param("t") Trace t, @Param("companyId") Long companyId,
			@Param("appId") Long appId);

}
