package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.manager.BaseTreeManager;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.service.sys.IAreaService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
	public List<Tree> findChildrenTreeNodes(Trace t, String areaRootCode, QueryRule statusRule) {
		Area root = service().fetchByGovCode(t, areaRootCode);
		if (root == null) {
			throw new BaseRuntimeException("10001", "areaRootCode参数错误：" + areaRootCode);
		}
		return service().findChildrenTreeNodes(t, root.getId(), statusRule);
	}

	@Override
	public List<Tree> findChildrenTreeNodes(Trace t, Long areaRootId, QueryRule statusRule) {
		return service().findChildrenTreeNodes(t, areaRootId, statusRule);
	}

	@Override
	public List<Area> findDirectChildren(Trace t, Long parentId, QueryRule statusRule) {
		return service().findDirectChildren(t, parentId, statusRule);
	}

	@Override
	public List<Tree> findDirectChildrenTreeNodes(Trace t, Long parentId, QueryRule statusRule) {
		return service().findDirectChildrenTreeNodes(t, parentId, statusRule);
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

	@Override
	public PermedAreaVo findValidAreaResourceByParent(Trace t, Long parentAreaId) {
		return service().findValidAreaResourceByParent(t, parentAreaId);
	}

	@Override
	public List<Tree> findChildrenTrees(Trace t, List<Long> areaRootIds, QueryRule statusRule) {
		return service().findChildrenTrees(t, areaRootIds, statusRule);
	}

}
