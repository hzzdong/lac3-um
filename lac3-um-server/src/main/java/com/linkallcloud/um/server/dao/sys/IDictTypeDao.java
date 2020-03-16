package com.linkallcloud.um.server.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.ITreeDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.DictType;

public interface IDictTypeDao extends ITreeDao<DictType> {

	//List<DictType> getChildrenByParentId(@Param("t") Trace t, @Param("parentId") Long parentId);

	List<DictType> getChildrenByTopParentId(@Param("t") Trace t, @Param("topParentId") Long topParentId);

}
