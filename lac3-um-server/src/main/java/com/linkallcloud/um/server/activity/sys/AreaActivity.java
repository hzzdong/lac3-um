package com.linkallcloud.um.server.activity.sys;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseTreeActivity;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Orderby;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.BeginsWith;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.query.rule.Less;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.server.dao.sys.IAreaDao;

@Component
public class AreaActivity extends BaseTreeActivity<Area, IAreaDao> implements IAreaActivity {

	@Autowired
	private IAreaDao areaDao;

	@Override
	public IAreaDao dao() {
		return areaDao;
	}

	@Override
	public Area fetchByGovCode(Trace t, String areaRootGovCode) {
		return areaDao.fetchByGovCode(t, areaRootGovCode);
	}

	@Override
	public int getChildrenCount(Trace t, Long parenId, QueryRule statusRule) {
		if (parenId == null) {
			return 0;
		}
		return dao().getChildrenCount(t, parenId, statusRule);
	}

	// @Cacheable(value = "AreaChildren", key = "#parentCode")
	@Override
	public List<Area> findChildren(Trace t, String parentCode, QueryRule statusRule) {
		if (Strings.isBlank(parentCode)) {
			return null;
		}
		return dao().findByParentCode(t, parentCode, parentCode.length(), statusRule);
	}

	@Override
	public Tree tree(Trace t) {
		List<Tree> result = super.getTreeNodes(t, true);
		Tree root = Trees.root0("中华人民共和国");
		root.setOpen(true);
		Trees.assembleTree(root, result);
		return root;
	}

	@Override
	public List<Tree> getTreeNodes(Trace t, boolean valid) {
		List<Tree> result = super.getTreeNodes(t, valid);
		Tree root = Trees.root0("中华人民共和国");
		root.setOpen(true);
		Trees.assembleTree(root, result);
		return root.getChildren();
	}

	@Override
	public Tree loadLevel1Tree(Trace t) {
		Tree root = Trees.root0("中华人民共和国");
		List<Area> areas = dao().findRootAreas(t, null);
		Trees.assembleDirectDomain(root, areas, null);
		return root;
	}

	@Override
	public List<Tree> loadTreeNodes(Trace t, Sid parentSid) {
		Long parentId = parentSid.getId();

		if (parentId != null && parentId.longValue() > 0) {
			List<Area> areas = findDirectChildren(t, parentId, null);
			return Trees.assembleDirectDomain(parentId.toString(), areas, null);
		} else if (parentId != null && parentId.longValue() == 0) {
			List<Area> areas = dao().findRootAreas(t, null);
			return Trees.assembleDirectDomain(parentId.toString(), areas, null);
		} else {
			Tree root = Trees.root0("中华人民共和国");
			return Arrays.asList(root);
		}
	}

	@Override
	public Tree findChildrenTree(Trace t, Long areaRootId, QueryRule statusRule) {
		if (areaRootId == null || areaRootId.equals(0L)) {
			return tree(t);
		}

		Area root = dao().fetchById(t, areaRootId);
		if (root == null) {
			throw new BaseRuntimeException("10001", "areaRootId参数错误：" + areaRootId);
		}

		Tree parent = root.toTreeNode();
		List<Area> areas = dao().findByParentCode(t, root.getCode(), root.getCode().length(), statusRule);
		if (areas != null && areas.size() > 0) {
			Trees.assembleDomain2Tree(parent, areas);
		}
		return parent;
	}

//	private List<Tree> castArea2TreeNode(Area root, List<Area> areas) {
//		if (areas != null && areas.size() > 0) {
//			List<Tree> result = new ArrayList<>();
//			for (int i = 0; i < areas.size(); i++) {
//				Area area = areas.get(i);
//				Tree tn = area.toTreeNode();
//				if (area.getId().equals(root.getId())) {
//					tn.setpId(null);
//				}
//				result.add(tn);
//			}
//			return result;
//		}
//		return null;
//	}

	@Override
	public Tree getTree(Trace t, boolean valid) {
		Tree tree = super.getTree(t, valid);
		tree.setOpen(true);
		tree.setName("中华人民共和国");
		return tree;
	}

	// @Cacheable(value = "Areas", key = "#parentCode + \"-\" + #levelLt ")
	@Override
	public List<Area> findByParentCodeAndLevel(Trace t, String parentCode, int levelLt) {
		if (Strings.isBlank(parentCode)) {
			return null;
		}

		Query qry = new Query();
		qry.addRule(new BeginsWith("govCodeLike", parentCode));// 浙江省
		qry.addRule(new Less("levelLt", levelLt));
		qry.addRule(new Equal("status", 0)); // 1禁用，不获取
		qry.setOrderby(new Orderby("sort,id", "asc,asc"));
		return this.find(t, qry);
	}

	// @Cacheable(value = "AreaDirectChildren", key = "#parentId")
	@Override
	public List<Area> findDirectChildren(Trace t, Long parentId, QueryRule statusRule) {
		if (parentId == null) {
			return null;
		}
		return dao().findByParent(t, parentId, statusRule);
	}

	@Override
	public List<Area> findPermedKhCompanyAppAreas(Trace t, Long khCompanyId, Long appId) {
		if (khCompanyId == null || appId == null) {
			return null;
		}
		return dao().findPermedKhCompanyAppAreas(t, khCompanyId, appId);
	}

	@Override
	public List<Area> findPermedYwCompanyAppAreas(Trace t, Long ywCompanyId, Long appId) {
		if (ywCompanyId == null || appId == null) {
			return null;
		}
		return dao().findPermedYwCompanyAppAreas(t, ywCompanyId, appId);
	}

}
