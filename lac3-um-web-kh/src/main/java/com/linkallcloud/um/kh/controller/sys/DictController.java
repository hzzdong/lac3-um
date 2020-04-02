package com.linkallcloud.um.kh.controller.sys;

import java.util.ArrayList;
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
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.iapi.sys.IDictManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;

@Controller
@RequestMapping(value = "/dict", method = RequestMethod.POST)
@Module(name = "数据字典")
public class DictController {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictManager dictManager;

	/**
	 * 根据DictType叶子节点的code，得到其具体字典数据，组装成DictType为根点的一棵树
	 * 
	 * @param code
	 * @param t
	 * @param av
	 * @return
	 * @throws IllegalParameterException
	 */
	@RequestMapping(value = "/loadDicts", method = RequestMethod.GET)
	public @ResponseBody Result<Object> loadDicts(@RequestParam(value = "code") String code, Trace t, AppVisitor av)
			throws IllegalParameterException {
		List<Tree> items = new ArrayList<Tree>();
		if (!Strings.isBlank(code)) {
			Tree tree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, code);
			if (tree != null) {
				items = tree.getChildren();
			}
		}
		return new Result<>(items);
	}

}
