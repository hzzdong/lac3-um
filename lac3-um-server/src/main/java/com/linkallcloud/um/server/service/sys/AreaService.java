package com.linkallcloud.um.server.service.sys;

import java.util.ArrayList;
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
import com.linkallcloud.um.dto.base.PermedAreaVo;
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

	// @Cacheable(value = "AreaChildrenTreeNodes", key = "#areaRootId + \"-\" +
	// #statusRule.toString()")
	@Override
	public List<Tree> findChildrenTreeNodes(Trace t, Long areaRootId, QueryRule statusRule) {
		return activity().findChildrenTreeNodes(t, areaRootId, statusRule);
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

	// @Cacheable(value = "AreaDirectChildrenTreeNodes", key = "#parentId")
	@Override
	public List<Tree> findDirectChildrenTreeNodes(Trace t, Long parentId, QueryRule statusRule) {
		return activity().findDirectChildrenTreeNodes(t, parentId, statusRule);
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
	public PermedAreaVo findValidAreaResourceByParent(Trace t, Long parentAreaId) {
		return activity().findValidAreaResourceByParent(t, parentAreaId);
	}

	@Override
	public Tree findChildrenTree(Trace t, Long areaRootId, QueryRule statusRule) {
		return activity().findChildrenTree(t, areaRootId, statusRule);
	}

	@Override
	public List<Tree> findChildrenTrees(Trace t, List<Long> areaRootIds, QueryRule statusRule) {
		List<Tree> result = new ArrayList<>();
		if (areaRootIds != null && areaRootIds.size() > 0) {
			for (Long areaId : areaRootIds) {
				Tree item = findChildrenTree(t, areaId, statusRule);
				result.add(item);
			}
		} else {
			Tree item = findChildrenTree(t, null, statusRule);
			result.add(item);
		}
		return result;
	}

}
