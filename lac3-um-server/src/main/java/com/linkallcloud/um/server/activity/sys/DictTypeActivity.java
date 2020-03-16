package com.linkallcloud.um.server.activity.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseTreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.um.activity.sys.IDictTypeActivity;
import com.linkallcloud.um.domain.sys.Dict;
import com.linkallcloud.um.domain.sys.DictType;
import com.linkallcloud.um.server.dao.sys.IDictDao;
import com.linkallcloud.um.server.dao.sys.IDictTypeDao;

@Component
public class DictTypeActivity extends BaseTreeActivity<DictType, IDictTypeDao> implements IDictTypeActivity {

    @Autowired
    private IDictTypeDao dictTypeDao;

    @Autowired
    private IDictDao dictDao;

    @Override
    public IDictTypeDao dao() {
        return dictTypeDao;
    }

    @Override
    protected void before(Trace t, boolean isNew, DictType entity) {
        super.before(t, isNew, entity);
        if (isNew) {
            if (entity.isTopParent()) {
                entity.setTopParentId(null);
            } else {
                DictType parent = dao().fetchById(t, entity.getParentId());
                if (parent != null) {
                    Long topParentId = Long.valueOf(parent.getCode().toString().split(parent.codeTag())[0]);
                    entity.setTopParentId(topParentId);
                }
            }
        }
    }

    @Override
    public DictType fetchByGovCode(Trace t, String dictTypeGovCode) {
        return dao().fetchByGovCode(t, dictTypeGovCode);
    }

    @Override
    public Tree getDictTypeTreeWithDictsById(Trace t, Long dictTypeId) {
        Tree root = null;
        DictType dt = dao().fetchById(t, dictTypeId);
        if (dt != null) {
            root = dt.toTreeNode();
            // root.setpId(null);

            List<DictType> children = dao().getChildrenByTopParentId(t, dt.getId());
            if (children != null && !children.isEmpty()) {
                Trees.assembleDomain2Tree(root, children);
            }

            List<Dict> dicts = dictDao.getDictsByTopTypeId(t, dt.getId());
            if (dicts != null && !dicts.isEmpty()) {
                Trees.assembleDomain2Tree(root, dicts);
            }
        }
        root.setpId(null);
        return root;
    }

    @Override
    public Tree getDirectDictsByTypeId(Trace t, Long dictTypeId) {
        Tree root = null;
        DictType dt = dao().fetchById(t, dictTypeId);
        if (dt != null) {
            root = dt.toTreeNode();
            // root.setpId(null);

            List<Dict> dicts = dictDao.getDictsByTypeId(t, dt.getId());
            if (dicts != null && !dicts.isEmpty()) {
                Trees.assembleDomain2Tree(root, dicts);
            }
        }
        root.setpId(null);
        return root;
    }
}
