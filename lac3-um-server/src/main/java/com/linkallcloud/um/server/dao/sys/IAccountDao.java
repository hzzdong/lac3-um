package com.linkallcloud.um.server.dao.sys;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Account;

public interface IAccountDao extends IDao<Account> {

    Account fecthByMobile(@Param("t") Trace t, @Param("mobile") String mobile);

    Account fecthByAccount(@Param("t") Trace t, @Param("account") String account);

    boolean updateLastLoginTime(@Param("t") Trace t, @Param("id") Long userId);

    Account fechByWechatOpenId(@Param("t") Trace t, @Param("openid") String openid);

    int updateAccountWechatOpenId(@Param("t") Trace t, @Param("id") Long id, @Param("openid") String openid);

}
