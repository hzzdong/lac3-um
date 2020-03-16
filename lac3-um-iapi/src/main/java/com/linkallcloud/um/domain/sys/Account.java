package com.linkallcloud.um.domain.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

import java.util.Date;

@ShowName("账号")
public class Account extends Domain {
    private static final long serialVersionUID = 1248490679564994775L;

    private String loginname;// 登录名
    private String name;// 姓名
    private String mobile;// 手机
    private String email;// 邮箱
    private Integer sex;// 性别
    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;// 生日
    private String ico;// 头像

    @JSONField(serialize = false)
    private String passwd;// 密码
    @JSONField(serialize = false)
    private String salt;// 密码盐
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate;// 最后登录时间

    @JSONField(serialize = false)
    private String oldPasswds;// 最近两次修改的老密码（MD5值）,用“,”分隔
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastPasswdDate;// 最后修改密码时间

    private String wechatOpenId;// 绑定微信ID
    private String alipayOpenId;// 绑定支付宝ID

    private String remark;// 描述

    public Account() {
        super();
    }

    public Account(String name, String mobile, String loginname, String password, String salt) {
        super();
        this.name = name;
        this.mobile = mobile;
        this.loginname = loginname;
        this.passwd = password;
        this.salt = salt;
    }

    public void desensitization() {
        this.passwd = null;
        this.salt = null;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getOldPasswds() {
        return oldPasswds;
    }

    public void setOldPasswds(String oldPasswds) {
        this.oldPasswds = oldPasswds;
    }

    public Date getLastPasswdDate() {
        return lastPasswdDate;
    }

    public void setLastPasswdDate(Date lastPasswdDate) {
        this.lastPasswdDate = lastPasswdDate;
    }

    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public String getAlipayOpenId() {
        return alipayOpenId;
    }

    public void setAlipayOpenId(String alipayOpenId) {
        this.alipayOpenId = alipayOpenId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
