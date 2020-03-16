package com.linkallcloud.um.pc.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.web.controller.BaseTreeLContorller;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.query.rule.NotEqual;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;

@Controller
@RequestMapping(value = "/area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaController extends BaseTreeLContorller<Area, IAreaManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

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

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> save(@RequestBody @Valid Area entity, Trace t, AppVisitor av) {
		try {
			doSave(entity, t, av);
			Area domain = manager().fetchById(t, entity.getId());
			return new Result<Object>(domain.toTreeNode());
		} catch (Throwable e) {
			return Exceptions.makeErrorResult(e);
		}
	}

	@Override
	protected Area doSave(Area entity, Trace t, AppVisitor av) {
		if (entity.getParentId() != null && entity.getParentId().longValue() != 0L) {
			Area parent = manager().fetchById(t, entity.getParentId());
			if (parent != null) {
				entity.setFullName(parent.getFullName() + entity.getName());
			}
		}
		return super.doSave(entity, t, av);
	}

	@RequestMapping(value = "/loadTree4App", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadTree4MyCompany(
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "appUuid", required = false) String appUuid, Trace t, AppVisitor av) {
		List<Tree> result = ywCompanyManager.getDefinedCompanyAreas(t, Long.parseLong(av.getCompanyId()),
				Long.parseLong(av.getAppId()));//(appId == null) ? Long.parseLong(av.getAppId()) : appId
		return new Result<List<Tree>>(result);
	}

	@Override
	protected List<Tree> doLoadTree(Trace t, AppVisitor av) {
		Long[] areaRootIds = ywCompanyManager.getCompanyAreaRootIds(t, Long.parseLong(av.getCompanyId()),
				Long.parseLong(av.getAppId()));
		List<Tree> result = new ArrayList<>();
		if (areaRootIds != null && areaRootIds.length > 0) {
			for (Long areaRootId : areaRootIds) {
				List<Tree> area1s = manager().findChildrenTreeNodes(t, areaRootId, new NotEqual("status", 9));
				if (area1s != null && !area1s.isEmpty()) {
					result.addAll(area1s);
				}
			}

		}

//		Long areaRootId = ywCompanyManager.getCompanyAreaRootId(t, Long.parseLong(av.getCompanyId()),
//				Long.parseLong(av.getAppId()));
//		List<Tree> result = manager().findChildrenTreeNodes(t, areaRootId, new NotEqual("status", 9));

		if (result != null && !result.isEmpty() && areaRootIds != null && areaRootIds.length == 1) {
			for (Tree node : result) {
				if (node.getId().equals(areaRootIds[0].toString())) {
					node.setOpen(true);
				}
			}
		}
		return result;
	}

	@RequestMapping(value = "/loadFullTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadFullTree(Trace t, AppVisitor av) {
		List<Tree> items = manager().getTreeNodes(t, false);
		return new Result<List<Tree>>(items);
	}

}
