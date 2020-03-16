package com.linkallcloud.um.dto.base;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.vo.Vo;

public class PermedAreaVo extends Vo {
    private static final long serialVersionUID = 1085403983834072506L;

    private Long parentAreaId;
    private String parentAreaName;

    private List<Tree> areaNodes;

    public Long getParentAreaId() {
        return parentAreaId;
    }

    public void setParentAreaId(Long parentAreaId) {
        this.parentAreaId = parentAreaId;
    }

    public String getParentAreaName() {
        return parentAreaName;
    }

    public void setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
    }

    public List<Tree> getAreaNodes() {
        return areaNodes;
    }

    public void setAreaNodes(List<Tree> areaNodes) {
        this.areaNodes = areaNodes;
    }

    public void addAreaNode(Tree areaNode) {
        if (this.areaNodes == null) {
            this.areaNodes = new ArrayList<>();
        }
        this.areaNodes.add(areaNode);
    }

}
