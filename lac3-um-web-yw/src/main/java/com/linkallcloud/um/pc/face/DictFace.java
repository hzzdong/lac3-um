package com.linkallcloud.um.pc.face;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.sys.Dict;
import com.linkallcloud.um.domain.sys.DictType;
import com.linkallcloud.um.iapi.sys.IDictManager;
import com.linkallcloud.um.iapi.sys.IDictTypeManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseTreeFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/Dict", method = RequestMethod.POST)
@Module(name = "数据字典")
public class DictFace extends BaseTreeFace<Dict, IDictManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictTypeManager dictTypeManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IDictManager dictManager;

	@Override
	protected IDictManager manager() {
		return dictManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadDictTree", method = RequestMethod.POST)
	public @ResponseBody Object loadDictTree(ObjectFaceRequest<String> fr, Trace t, AppVisitor av) {
		Tree root = dictTypeManager.getTree(t, true);
		return Arrays.asList(root);
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])保存了 数据字典类型 [(${fr.data.name})], TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/saveDictTree", method = RequestMethod.POST)
	public @ResponseBody Object saveDictTree(ObjectFaceRequest<DictType> fr, Trace t, SessionUser su) {
		DictType entity = fr.getData();
		if (entity.getId() != null && !Strings.isBlank(entity.getUuid())) {
			dictTypeManager.update(t, entity);
		} else {
			entity.setUuid(entity.generateUuid());
			Long id = dictTypeManager.insert(t, entity);
			entity.setId(id);
		}
		return entity.toTreeNode();
	}

	@Face(simple = true)
	@RequestMapping(value = "/deleteDictTree", method = RequestMethod.POST)
	@WebLog(db = true, desc = "用户([(${su.sid.name})])删除了 数据字典类型 [(${fr.id})], TID:[(${tid})]")
	public @ResponseBody Object deleteDictTree(IdFaceRequest fr, Trace t, SessionUser su) {
		if (fr.getId() == null || Strings.isBlank(fr.getUuid())) {
			throw new BizException(Exceptions.CODE_ERROR_DELETE, "参数错误");
		}
		return dictTypeManager.delete(t, fr.getId(), fr.getUuid());
	}

	/**
	 * 根据DictType叶子节点的code，得到其具体字典数据，组装成DictType为根点的一棵树
	 * 
	 * @param fr
	 * @param t
	 * @param av
	 * @return
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
