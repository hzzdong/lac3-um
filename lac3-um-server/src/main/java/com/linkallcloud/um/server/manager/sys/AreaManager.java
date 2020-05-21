package com.linkallcloud.um.server.manager.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseTreeManager;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.service.sys.IAreaService;

@Service(interfaceClass = IAreaManager.class, version = "${dubbo.service.version}")
@Module(name = "区域")
public class AreaManager extends BaseTreeManager<Area, IAreaService> implements IAreaManager {

	@Autowired
	private IAreaService areaService;

	@Override
	protected IAreaService service() {
		return areaService;
	}

	@Override
	public Area fetchByGovCode(Trace t, String areaRootGovCode) {
		return service().fetchByGovCode(t, areaRootGovCode);
	}

	@Override
	public int getChildrenCount(Trace t, Long parenId, QueryRule statusRule) {
		return service().getChildrenCount(t, parenId, statusRule);
	}

	@Override
	public List<Area> findChildren(Trace t, String parentCode, QueryRule statusRule) {
		return service().findChildren(t, parentCode, statusRule);
	}

	@Override
	public List<Area> findDirectChildren(Trace t, Long parentId, QueryRule statusRule) {
		return service().findDirectChildren(t, parentId, statusRule);
	}

	@Override
	public List<Area> findByParentCodeAndLevel(Trace t, String parentCode, int levelLt) {
		return service().findByParentCodeAndLevel(t, parentCode, levelLt);
	}

	@Override
	public List<Area> findPermedKhCompanyAppAreas(Trace t, Long khCompanyId, Long appId) {
		return service().findPermedKhCompanyAppAreas(t, khCompanyId, appId);
	}

	@Override
	public List<Area> findPermedYwCompanyAppAreas(Trace t, Long ywCompanyId, Long appId) {
		return service().findPermedYwCompanyAppAreas(t, ywCompanyId, appId);
	}

}
