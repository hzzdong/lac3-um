package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class ModifyPasswordRequest extends FaceRequest {
    private static final long serialVersionUID = 1124215009957281259L;

    private String account;
    private String oldPwd;
    private String newPwd;

    public ModifyPasswordRequest() {
        super();
    }

    public ModifyPasswordRequest(String account, String oldPwd, String newPwd) {
        this.account = account;
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
