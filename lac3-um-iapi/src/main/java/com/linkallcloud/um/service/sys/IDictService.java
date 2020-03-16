package com.linkallcloud.um.service.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.ITreeService;
import com.linkallcloud.um.domain.sys.Dict;

import java.util.List;

public interface IDictService extends ITreeService<Dict> {
	
	/**
	 * 根据dictTypeId，得到其下直接的字典数据
	 * 
	 * @param t
	 * @param dictTypeId
	 * @return
	 */
	List<Dict> findDictsByDictTypeId(Trace t, Long dictTypeId);
	
	/**
	 * 根据topDictTypeId，得到其下所有分类的字段数据
	 * 
	 * @param t
	 * @param topDictTypeId
	 * @return
	 */
	List<Dict> findDictsByDictTopTypeId(Trace t, Long topDictTypeId);
	
	void cleanDictsTopCache(Trace t, Long topDictTypeId);
	void cleanDictsCache(Trace t, Long dictTypeId);

}
