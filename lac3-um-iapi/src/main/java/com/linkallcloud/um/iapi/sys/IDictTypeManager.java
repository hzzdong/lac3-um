package com.linkallcloud.um.iapi.sys;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.ITreeManager;
import com.linkallcloud.um.domain.sys.DictType;

public interface IDictTypeManager extends ITreeManager<DictType> {

	/**
	 * 根据DictType根节点的code，得到其下整棵树，包括类型树和具体字典数据
	 * 
	 * @param t
	 * @param topDictTypeCode
	 * @return
	 */
	Tree getDictTypeTreeWithDictsByTopCode(Trace t, String topDictTypeCode);

	/**
	 * 根据DictType叶子节点的code，得到其具体字典数据，组装成DictType为根点的一棵树
	 * 
	 * @param t
	 * @param leafDictTypeCode
	 * @return
	 */
	Tree getDictTypeTreeWithDictsByLeafCode(Trace t, String leafDictTypeCode);

}
