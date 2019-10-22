package com.linkallcloud.um.server.activity.sys;

import com.linkallcloud.core.activity.BaseTreeActivity;
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
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.server.dao.sys.IAreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public List<Tree> getTreeNodes(Trace t, boolean valid) {
        List<Tree> result = super.getTreeNodes(t, valid);
        Tree root = new Tree("0", null, "中华人民共和国");
        root.setOpen(true);
        result = Trees.assembleChildren2Parent(result, root);
        return result;
    }

    // @Cacheable(value = "AreaChildrenTreeNodes", key = "#areaRootId + \"-\" +
    // #statusRule.toString()")
    @Override
    public List<Tree> findChildrenTreeNodes(Trace t, Long areaRootId, QueryRule statusRule) {
        if (areaRootId == null || areaRootId.equals(0L)) {
            return getTreeNodes(t, true);
        }

        Area root = dao().fetchById(t, areaRootId);
        if (root == null) {
            throw new BaseRuntimeException("10001", "areaRootId参数错误：" + areaRootId);
        }

        List<Area> areas = dao().findByParentCode(t, root.getCode(), root.getCode().length(), statusRule);
        if (areas != null && areas.size() > 0) {
            List<Tree> treeNodes = castArea2TreeNode(root, areas);
            return Trees.filterTreeNode(treeNodes);
        }
        return null;
    }

    private List<Tree> castArea2TreeNode(Area root, List<Area> areas) {
        if (areas != null && areas.size() > 0) {
            List<Tree> result = new ArrayList<>();
            for (int i = 0; i < areas.size(); i++) {
                Area area = areas.get(i);
                Tree tn = area.toTreeNode();
                if (area.getId().equals(root.getId())) {
                    tn.setpId(null);
                }
                result.add(tn);
            }
            return result;
        }
        return null;
    }

    @Override
    public Tree getTree(Trace t, boolean valid) {
        Tree tree = super.getTree(t, valid);
        tree.setOpen(true);
        tree.setId("AREA_ROOT");
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

    // @Cacheable(value = "AreaDirectChildrenTreeNodes", key = "#parentId")
    @Override
    public List<Tree> findDirectChildrenTreeNodes(Trace t, Long parentId, QueryRule statusRule) {
        List<Tree> result = new ArrayList<>();
        if (parentId != null) {
            List<Area> areas = dao().findByParent(t, parentId, statusRule);
            if (areas != null && !areas.isEmpty()) {
                for (Area area : areas) {
                    Tree node = area.toTreeNode();
                    node.setpId(null);
                    result.add(node);
                }
            }
        }
        return result;
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

    @Override
    public PermedAreaVo findValidAreaResourceByParent(Trace t, Long parentAreaId) {
        if (parentAreaId == null) {
            return null;
        }
        List<Area> areas = areaDao.findByParent(t, parentAreaId, new Equal("status", 0));
        return assemblePermedAreaVo(t, parentAreaId, areas);
    }

    protected PermedAreaVo assemblePermedAreaVo(Trace t, Long parentAreaId, List<Area> areas) {
        PermedAreaVo result = new PermedAreaVo();
        result.setParentAreaId(parentAreaId);
        if (0L != result.getParentAreaId()) {
            Area parent = areaDao.fetchById(t, result.getParentAreaId());
            if (parent != null) {
                result.setParentAreaName(parent.getName());
            }
        } else {
            result.setParentAreaName("中华人民共和国");
        }

        if (areas != null && !areas.isEmpty()) {
            List<Tree> anodes = new ArrayList<>();
            for (int i = 0; i < areas.size(); i++) {
                Tree tn = areas.get(i).toTreeNode();
                tn.setpId(null);
                anodes.add(tn);
            }
            result.setAreaNodes(Trees.filterTreeNode(anodes));
        }
        return result;
    }
}
