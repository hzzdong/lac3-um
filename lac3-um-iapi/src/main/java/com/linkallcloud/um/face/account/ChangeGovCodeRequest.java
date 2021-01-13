package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class ChangeGovCodeRequest extends FaceRequest {
    private static final long serialVersionUID = -1969833276911235366L;

    private String uuid;
    private String govCode;

    public ChangeGovCodeRequest() {
        super();
    }

    public ChangeGovCodeRequest(String uuid, String govCode) {
        super();
        this.uuid = uuid;
        this.govCode = govCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGovCode() {
        return govCode;
    }

    public void setGovCode(String govCode) {
        this.govCode = govCode;
    }
}