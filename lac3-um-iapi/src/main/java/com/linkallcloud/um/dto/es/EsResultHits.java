package com.linkallcloud.um.dto.es;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class EsResultHits implements Serializable {
    private static final long serialVersionUID = 3668368030380545123L;
    
    @JSONField(name="_id")
    private String id;
    
    @JSONField(name="_index")
    private String index;
    
    @JSONField(name="_type")
    private String type;
    
    @JSONField(name="_score")
    private Float score;
    
    @JSONField(name="_source")
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
