package com.linkallcloud.um.domain.party;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName("客户用户")
public class KhUser extends User {
    private static final long serialVersionUID = -1491375903174619861L;

    public KhUser() {
        super();
    }

    public KhUser(String name, String account, String mobile, String password) {
        super(name, account, mobile, password);
    }
    //
    // public KhUser(KhUserVo vo) {
    // super();
    // this.setId(vo.getId());
    // this.setUuid(vo.getUuid());
    // this.setName(vo.getName());
    // this.setMobile(vo.getMobile());
    // this.setEmail(vo.getEmail());
    // this.setIdCard(vo.getIdCard());
    // this.setSex(vo.getSex());
    // this.setBirthday(vo.getBirthday());
    // }
    //
    // @SuppressWarnings("unchecked")
    // @Override
    // public <V extends Vo> V toVo(Class<V> classV) {
    // KhUserVo vo = new KhUserVo();
    // vo.setId(this.getId());
    // vo.setUuid(this.getUuid());
    // vo.setName(this.getName());
    // vo.setMobile(this.getMobile());
    // vo.setEmail(this.getMobile());
    // vo.setIdCard(this.getIdCard());
    // vo.setSex(this.getSex());
    // vo.setBirthday(this.getBirthday());
    // vo.setAccount(this.getAccount());
    // return (V) vo;
    // }

}
