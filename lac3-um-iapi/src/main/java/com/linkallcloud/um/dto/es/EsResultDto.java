package com.linkallcloud.um.dto.es;

import java.io.Serializable;
import java.util.List;

public class EsResultDto implements Serializable {
    private static final long serialVersionUID = -1371039780468002506L;

    private EsResultParentHits hits;

    public EsResultParentHits getHits() {
        return hits;
    }

    public void setHits(EsResultParentHits hits) {
        this.hits = hits;
    }

    public long getRecordsTotal() {
        if (hits != null && hits.getTotal() != null && hits.getTotal().getValue() != null) {
            return hits.getTotal().getValue();
        }
        return 0;
    }

    public List<EsResultHits> getData() {
        if (hits != null && hits.getHits() != null && hits.getHits().size() > 0) {
            return hits.getHits();
        }
        return null;
    }

}
