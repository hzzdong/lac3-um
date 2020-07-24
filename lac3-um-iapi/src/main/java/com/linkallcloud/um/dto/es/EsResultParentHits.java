package com.linkallcloud.um.dto.es;

import java.io.Serializable;
import java.util.List;

public class EsResultParentHits implements Serializable {
    private static final long serialVersionUID = -7420743495377818433L;

    private List<EsResultHits> hits;

    private EsResultTotal total;

    public EsResultTotal getTotal() {
        return total;
    }

    public void setTotal(EsResultTotal total) {
        this.total = total;
    }

    public List<EsResultHits> getHits() {
        return hits;
    }

    public void setHits(List<EsResultHits> hits) {
        this.hits = hits;
    }

}
