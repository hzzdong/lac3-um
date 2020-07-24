package com.linkallcloud.um.iapi.es;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;

public interface IEsManager<T> {

    Page<T> find(Trace t, Page<T> page);

    // String cat(String index);
    //
     boolean deleteIndex(String index);
}
