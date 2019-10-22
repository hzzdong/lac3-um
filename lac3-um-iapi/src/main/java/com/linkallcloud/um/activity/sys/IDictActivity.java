package com.linkallcloud.um.activity.sys;

import com.linkallcloud.core.activity.ITreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Dict;

import java.util.List;

public interface IDictActivity extends ITreeActivity<Dict> {

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

}
