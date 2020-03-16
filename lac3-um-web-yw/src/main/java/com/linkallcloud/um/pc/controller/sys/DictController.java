package com.linkallcloud.um.pc.controller.sys;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.um.domain.sys.Dict;
import com.linkallcloud.um.domain.sys.DictType;
import com.linkallcloud.um.iapi.sys.IDictManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;

@Controller
@RequestMapping(value = "/dict", method = RequestMethod.POST)
@Module(name = "数据字典")
public class DictController {

	private Log log = Logs.get();

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictManager dictManager;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String list() {
		return "page/dict/main";
	}

	@RequestMapping(value = "/loadDictTypeTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadTree(Trace t, AppVisitor av) throws IllegalParameterException {
		List<Tree> items = dictTypeManager.getTreeNodes(t, false);
		return new Result<>(items);
	}

	@RequestMapping(value = "/addDictType", method = RequestMethod.GET)
	public String addDictType(@RequestParam(value = "parentId", required = false) Long parentId, Trace t,
			ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		return "page/dict/DictType";
	}

	@RequestMapping(value = "/editDictType", method = RequestMethod.GET)
	public String editDictType(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return "page/dict/DictType";
	}

	@RequestMapping(value = "/getDictType", method = RequestMethod.GET)
	public @ResponseBody Result<DictType> getDictType(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t) {
		DictType entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = dictTypeManager.fetchByIdUuid(t, id, uuid);
		} else {
			entity = new DictType();
			entity.setParentId(parentId);
			if (!entity.isTopParent()) {
				DictType parent = dictTypeManager.fetchById(t, parentId);
				if (parent != null) {
					entity.setParentName(parent.getName());
				}
			}
		}
		if (entity.isTopParent()) {
			entity.setParentName("所有数据字典类型");
		}
		return new Result<>(entity);
	}

	@WebLog(db = true)
	@RequestMapping(value = "/saveDictType", method = RequestMethod.POST)
	public @ResponseBody Result<DictType> saveDictType(@RequestBody @Valid DictType entity, Trace t) {
		if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
			dictTypeManager.update(t, entity);
		} else {
			Long id = dictTypeManager.insert(t, entity);
			entity.setId(id);
		}
		return new Result<>(entity);
	}

	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了字典分类([(${id})]), TID:[(${tid})]")
	@RequestMapping(value = "/deleteDictType", method = RequestMethod.POST)
	public @ResponseBody Result<Object> deleteDictType(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t) {
		Boolean ret = dictTypeManager.delete(t, id, uuid);
		return new Result<>(!ret.booleanValue(), "9010", "删除对象失败");
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody Result<Page<Dict>> page(@RequestBody WebPage webPage, Trace t) {
		Page<Dict> page = dictManager.findPage(t, webPage.toPage());
		return new Result<>(page);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam(value = "parentId", required = false) Long parentId, Trace t, ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		modelMap.put("id", null);
		modelMap.put("uuid", null);
		return "page/dict/Dict";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
		modelMap.put("parentId", parentId);
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return "page/dict/Dict";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Result<Dict> get(@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "uuid", required = false) String uuid, Trace t) {
		Dict entity = null;
		if (id != null && id > 0 && uuid != null) {
			entity = dictManager.fetchByIdUuid(t, id, uuid);
		} else {
			entity = new Dict();
			entity.setParentId(parentId);
			if (parentId != null && parentId > 0L) {
				DictType dt = dictTypeManager.fetchById(t, parentId);
				if (dt != null) {
					entity.setParentName(dt.getName());
				}
			}
		}
		return new Result<>(entity);
	}

	@WebLog(db = true)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Result<Dict> save(@RequestBody @Valid Dict entity, Trace t) {
		if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
			dictManager.update(t, entity);
			log.info("DictController: Update Dict success, id:" + entity.getId());
		} else {
			Long id = dictManager.insert(t, entity);
			entity.setId(id);
			log.info("DictController: Insert Dict success, id:" + id);
		}
		return new Result<>(entity);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@WebLog(db = true, desc = "用户([(${visitor.name})])删除了字典([(${id})]), TID:[(${tid})]")
	public @ResponseBody Result<Object> delete(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t) {
		Boolean ret = dictManager.delete(t, id, uuid);
		return new Result<>(!ret.booleanValue(), "9010", "删除对象失败");
	}

}
