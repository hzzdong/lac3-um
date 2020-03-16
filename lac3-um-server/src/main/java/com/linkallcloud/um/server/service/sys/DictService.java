package com.linkallcloud.um.server.service.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseTreeService;
import com.linkallcloud.um.activity.sys.IDictActivity;
import com.linkallcloud.um.domain.sys.Dict;
import com.linkallcloud.um.service.sys.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Module(name = "数据字典")
@Service
@Transactional(readOnly = true)
public class DictService extends BaseTreeService<Dict, IDictActivity> implements IDictService {

    @Autowired
    private IDictActivity dictActivity;

    @Override
    public IDictActivity activity() {
        return dictActivity;
    }

    @Cacheable(value = "Dicts", key = "#dictTypeId")
    @Override
    public List<Dict> findDictsByDictTypeId(Trace t, Long dictTypeId) {
        return activity().findDictsByDictTypeId(t, dictTypeId);
    }

    @Cacheable(value = "Dicts-Top", key = "#topDictTypeId")
    @Override
    public List<Dict> findDictsByDictTopTypeId(Trace t, Long topDictTypeId) {
        return activity().findDictsByDictTopTypeId(t, topDictTypeId);
    }

    @CacheEvict(value = "Dicts-Top", key = "#topDictTypeId")
    @Override
    public void cleanDictsTopCache(Trace t, Long topDictTypeId) {
    }

    @CacheEvict(value = "Dicts", key = "#dictTypeId")
    @Override
    public void cleanDictsCache(Trace t, Long dictTypeId) {
    }

}
