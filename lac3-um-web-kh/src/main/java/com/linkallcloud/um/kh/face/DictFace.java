package com.linkallcloud.um.kh.face;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.iapi.sys.IDictManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;
import com.linkallcloud.web.face.annotation.Face;

@Controller
@RequestMapping(value = "/face/Dict", method = RequestMethod.POST)
@Module(name = "数据字典")
public class DictFace {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
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
	@Face(simple = true)
	@RequestMapping(value = "/loadDicts", method = RequestMethod.POST)
	public @ResponseBody Object loadDicts(ObjectFaceRequest<String> fr, Trace t, AppVisitor av) {
		String code = fr.getData();
		List<Tree> items = new ArrayList<Tree>();
		if (!Strings.isBlank(code)) {
			Tree tree = dictTypeManager.getDictTypeTreeWithDictsByLeafCode(t, code);
			if (tree != null) {
				items = tree.getChildren();
			}
		}
		return items;
	}

}
