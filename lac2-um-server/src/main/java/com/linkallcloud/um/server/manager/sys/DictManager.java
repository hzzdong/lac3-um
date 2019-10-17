package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.linkallcloud.busilog.annotation.Module;
import com.linkallcloud.dto.Trace;
import com.linkallcloud.exception.BaseRuntimeException;
import com.linkallcloud.manager.BaseManager;
import com.linkallcloud.um.domain.sys.Dict;
import com.linkallcloud.um.domain.sys.DictType;
import com.linkallcloud.um.iapi.sys.IDictManager;
import com.linkallcloud.um.server.service.sys.IDictService;
import com.linkallcloud.um.server.service.sys.IDictTypeService;

@Service(interfaceClass = IDictManager.class, version = "${dubbo.service.version}")
@Module(name = "数据字典")
public class DictManager extends BaseManager<Long, Dict, IDictService> implements IDictManager {

	@Autowired
	private IDictService dictService;

	@Autowired
	private IDictTypeService dictTypeService;

	@Override
	protected IDictService service() {
		return dictService;
	}

	@Override
	public List<Dict> findDictsByDictTypeId(Trace t, Long dictTypeId) {
		return service().findDictsByDictTypeId(t, dictTypeId);
	}

	@Override
	public List<Dict> findDictsByDictTypeCode(Trace t, String dictTypeCode) {
		DictType type = dictTypeService.fetchByGovCode(t, dictTypeCode);
		if (type != null) {
			return findDictsByDictTypeId(t, type.getId());
		}
		return null;
	}

	@Override
	public List<Dict> findDictsByDictTopTypeId(Trace t, Long topDictTypeId) {
		return service().findDictsByDictTopTypeId(t, topDictTypeId);
	}

	@Override
	public List<Dict> findDictsByDictTopTypeCode(Trace t, String topDictTypeCode) {
		DictType type = dictTypeService.fetchByGovCode(t, topDictTypeCode);
		if (type != null) {
			return findDictsByDictTopTypeId(t, type.getId());
		}
		return null;
	}

	@Override
	public boolean update(Trace t, Dict entity) throws BaseRuntimeException {
		boolean result = super.update(t, entity);
		if (result) {
			cleanDictsCache(t, entity.getId());
		}
		return result;
	}

	@Override
	public boolean updateStatus(Trace t, int status, Long id, String uuid) throws BaseRuntimeException {
		boolean result = super.updateStatus(t, status, id, uuid);
		if (result) {
			cleanDictsCache(t, id);
		}
		return result;
	}

	@Override
	public boolean delete(Trace t, Long id, String uuid) throws BaseRuntimeException {
		cleanDictsCache(t, id);
		return super.delete(t, id, uuid);
	}

	private void cleanDictsCache(Trace t, Long dictId) {
		Dict entity = service().fetchById(t, dictId);
		if (entity != null) {
			Long topDictTypeId = entity.getTopTypeId();
			dictTypeService.cleanDictTypeCache(t, topDictTypeId);
			dictTypeService.cleanDictCache(t, entity.getParentId());

			service().cleanDictsTopCache(t, topDictTypeId);
			service().cleanDictsCache(t, entity.getParentId());
		}
	}

}
