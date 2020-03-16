package com.linkallcloud.um.face.account;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class AccountValidateRequest extends FaceRequest {
    private static final long serialVersionUID = -3546050383758784423L;

    private String account;
    private String password;

    private String vcode;

    public AccountValidateRequest(String account, String password, String vcode) {
        this.account = account;
        this.password = password;
        this.vcode = vcode;
    }

    public AccountValidateRequest() {
        super();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

}
