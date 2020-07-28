package com.linkallcloud.um.server.manager.sys;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.manager.BaseTreeManager;
import com.linkallcloud.um.domain.sys.DictType;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;
import com.linkallcloud.um.service.sys.IDictTypeService;

@DubboService(interfaceClass = IDictTypeManager.class, version = "${dubbo.service.version}")
@Module(name = "数据字典类型")
public class DictTypeManager extends BaseTreeManager<DictType, IDictTypeService> implements IDictTypeManager {

	@Autowired
	private IDictTypeService dictTypeService;

	@Override
	protected IDictTypeService service() {
		return dictTypeService;
	}

	@Override
	public Tree getDictTypeTreeWithDictsByTopCode(Trace t, String topDictTypeCode) {
		DictType type = service().fetchByGovCode(t, topDictTypeCode);
		return service().getDictTypeTreeWithDictsById(t, type.getId());
	}

	@Override
	public Tree getDictTypeTreeWithDictsByLeafCode(Trace t, String leafDictTypeCode) {
		DictType type = service().fetchByGovCode(t, leafDictTypeCode);
		return service().getDirectDictsByTypeId(t, type.getId());
	}

	@Override
	public boolean update(Trace t, DictType entity) throws BaseRuntimeException {
		boolean result = super.update(t, entity);
		if (result) {
			//cleanDictOrTypeCache(t, entity);
		}
		return result;
	}

	@Override
	public boolean updateStatus(Trace t, int status, Long id, String uuid) throws BaseRuntimeException {
		boolean result = super.updateStatus(t, status, id, uuid);
		if (result) {
			DictType entity = service().fetchByIdUuid(t, id, uuid);
			if (entity != null) {
				//cleanDictOrTypeCache(t, entity);
			}
		}
		return result;
	}

	@Override
	public boolean delete(Trace t, Long id, String uuid) throws BaseRuntimeException {
		DictType entity = service().fetchByIdUuid(t, id, uuid);
		if (entity != null) {
			//cleanDictOrTypeCache(t, entity);
		}
		return super.delete(t, id, uuid);
	}

//	private void cleanDictOrTypeCache(Trace t, DictType entity) {
//		Long topDictTypeId = entity.isTopParent() ? entity.getId() : entity.getTopParentId();
//		service().cleanDictTypeCache(t, topDictTypeId);
//		service().cleanDictCache(t, entity.getId());
//	}

}
