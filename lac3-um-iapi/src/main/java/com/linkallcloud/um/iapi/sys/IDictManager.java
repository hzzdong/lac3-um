package com.linkallcloud.um.iapi.sys;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.ITreeManager;
import com.linkallcloud.um.domain.sys.Dict;

public interface IDictManager extends ITreeManager<Dict> {

	/**
	 * 根据dictTypeId，得到其下直接的字典数据
	 * 
	 * @param t
	 * @param dictTypeId
	 * @return
	 */
	List<Dict> findDictsByDictTypeId(Trace t, Long dictTypeId);
	
	/**
	 * 根据dictTypeCode，得到其下直接的字典数据
	 * 
	 * @param t
	 * @param dictTypeId
	 * @return
	 */
	List<Dict> findDictsByDictTypeCode(Trace t, String dictTypeCode);

	/**
	 * 根据topDictTypeId，得到其下所有分类的字段数据
	 * 
	 * @param t
	 * @param topDictTypeId
	 * @return
	 */
	List<Dict> findDictsByDictTopTypeId(Trace t, Long topDictTypeId);
	
	/**
	 * 根据topDictTypeCode，得到其下所有分类的字段数据
	 * 
	 * @param t
	 * @param topDictTypeId
	 * @return
	 */
	List<Dict> findDictsByDictTopTypeCode(Trace t, String topDictTypeCode);

}
