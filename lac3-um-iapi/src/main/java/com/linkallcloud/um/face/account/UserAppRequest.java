package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class UserAppRequest extends FaceRequest {
    private static final long serialVersionUID = -1969833276911235366L;

    private Long userId;
    private Long appId;

    public UserAppRequest(Long userId, Long appId) {
        this.userId = userId;
        this.appId = appId;
    }

    public UserAppRequest() {
        super();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
