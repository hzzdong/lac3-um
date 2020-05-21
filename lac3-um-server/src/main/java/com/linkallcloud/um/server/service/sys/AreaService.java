package com.linkallcloud.um.server.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.core.service.BaseTreeService;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.service.sys.IAreaService;

@Module(name = "区域")
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseTreeService<Area, IAreaActivity> implements IAreaService {

	@Autowired
	private IAreaActivity areaActivity;

	@Override
	public IAreaActivity activity() {
		return areaActivity;
	}

	@Override
	public Area fetchByGovCode(Trace t, String areaRootGovCode) {
		return activity().fetchByGovCode(t, areaRootGovCode);
	}

	@Override
	public int getChildrenCount(Trace t, Long parenId, QueryRule statusRule) {
		return activity().getChildrenCount(t, parenId, statusRule);
	}

	// @Cacheable(value = "AreaChildren", key = "#parentCode")
	@Override
	public List<Area> findChildren(Trace t, String parentCode, QueryRule statusRule) {
		return activity().findChildren(t, parentCode, statusRule);
	}

	@Override
	public List<Tree> getTreeNodes(Trace t, boolean valid) {
		return activity().getTreeNodes(t, valid);
	}

	@Override
	public Tree getTree(Trace t, boolean valid) {
		return activity().getTree(t, valid);
	}

	// @Cacheable(value = "Areas", key = "#parentCode + \"-\" + #levelLt ")
	@Override
	public List<Area> findByParentCodeAndLevel(Trace t, String parentCode, int levelLt) {
		return activity().findByParentCodeAndLevel(t, parentCode, levelLt);
	}

	// @Cacheable(value = "AreaDirectChildren", key = "#parentId")
	@Override
	public List<Area> findDirectChildren(Trace t, Long parentId, QueryRule statusRule) {
		return activity().findDirectChildren(t, parentId, statusRule);
	}

	@Override
	public List<Area> findPermedKhCompanyAppAreas(Trace t, Long khCompanyId, Long appId) {
		return activity().findPermedKhCompanyAppAreas(t, khCompanyId, appId);
	}

	@Override
	public List<Area> findPermedYwCompanyAppAreas(Trace t, Long ywCompanyId, Long appId) {
		return activity().findPermedYwCompanyAppAreas(t, ywCompanyId, appId);
	}

	@Override
	public Tree findChildrenTree(Trace t, Long areaRootId, QueryRule statusRule) {
		return activity().findChildrenTree(t, areaRootId, statusRule);
	}

}
