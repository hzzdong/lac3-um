package com.linkallcloud.um.kh.controller.sys;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;
import com.linkallcloud.web.controller.BaseTreeLContorller;

@Controller
@RequestMapping(value = "/area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaController extends BaseTreeLContorller<Area, IAreaManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhSystemConfigManager khSystemConfigManager;

	@Override
	protected IAreaManager manager() {
		return areaManager;
	}

	@Override
	protected Area doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		Area entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = areaManager.fetchByIdUuid(t, id, uuid);
		} else {
			entity = new Area();
			entity.setParentId(parentId);
			if (!entity.isTopParent()) {
				Area parent = areaManager.fetchById(t, parentId);
				if (parent != null) {
					entity.setParentName(parent.getName());
				}
			}
		}
		if (entity.isTopParent()) {
			entity.setParentName("中华人民共和国");
		}
		return entity;
	}

	@RequestMapping(value = "/loadTree4App", method = RequestMethod.GET)
	public @ResponseBody Result<Object> loadTree4MyCompany(@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "appUuid", required = false) String appUuid, Trace t, AppVisitor av) {
		List<Tree> result = khCompanyManager.getDefinedCompanyAreas(t, Long.parseLong(av.getCompanyId()),
				(appId == null) ? Long.parseLong(av.getAppId()) : appId);
		return new Result<Object>(result);
	}

	@Override
	protected List<Tree> doLoadTree(Trace t, AppVisitor av) {
		KhSystemConfig sc = khSystemConfigManager.fetchByCompanyId(t, Long.parseLong(av.getCompanyId()));
		Long areaRootId = 0L;
		if (sc != null && sc.getRootAreaId() != null) {
			areaRootId = sc.getRootAreaId();
		}
		List<Tree> result = manager().findChildrenTreeNodes(t, areaRootId, new Equal("status", 0));
		if (result != null && !result.isEmpty()) {
			for (Tree node : result) {
				if (node.getId().equals(areaRootId.toString())) {
					node.setOpen(true);
				}
			}
		}
		return result;
	}

	@RequestMapping(value = "/loadTree4MyCompany", method = RequestMethod.GET)
	public @ResponseBody Result<Object> loadTree4MyCompany(Trace t, AppVisitor av) {
		List<Tree> items = doLoadTree(t, av);
		return new Result<Object>(items);
	}

	@RequestMapping(value = "/loadFullTree", method = RequestMethod.GET)
	public @ResponseBody Result<Object> loadFullTree(Trace t, AppVisitor av) {
		List<Tree> items = manager().getTreeNodes(t, false);
		return new Result<Object>(items);
	}

}
