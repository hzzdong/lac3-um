package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.ITreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.domain.sys.DictType;

public interface IDictTypeActivity extends ITreeActivity<DictType> {

    DictType fetchByGovCode(Trace t, String dictTypeGovCode);

    /**
     * 根据DictType根节点的ID，得到其下整棵树，包括类型树和具体字典数据
     *
     * @param t
     * @param dictTypeId
     * @return
     */
    Tree getDictTypeTreeWithDictsById(Trace t, Long dictTypeId);
    //Tree getDictTypeTreeWithDictsByTopTypeCode(Trace t, String topDictTypeCode);

    /**
     * 根据DictType叶子节点的ID，得到其具体字典数据，组装成DictType为根点的一棵树
     *
     * @param t
     * @param dictTypeId
     * @return
     */
    Tree getDirectDictsByTypeId(Trace t, Long dictTypeId);
    //Tree getDirectDictsByLeafTypeCode(Trace t, String leafDictTypeCode);
}
