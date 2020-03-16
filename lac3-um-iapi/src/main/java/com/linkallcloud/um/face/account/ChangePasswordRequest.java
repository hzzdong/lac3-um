package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class ChangePasswordRequest extends FaceRequest {
    private static final long serialVersionUID = 587511485012088440L;

    private Long id;
    private String uuid;
    private String oldPwd;
    private String newPwd;

    public ChangePasswordRequest() {
        super();
    }

    public ChangePasswordRequest(Long id, String uuid, String oldPwd, String newPwd) {
        this.id = id;
        this.uuid = uuid;
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

}
