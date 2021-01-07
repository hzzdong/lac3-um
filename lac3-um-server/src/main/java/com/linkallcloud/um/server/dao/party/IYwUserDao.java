package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.YwUser;

public interface IYwUserDao extends IUserDao<YwUser> {

    List<YwUser> findOrgUsers(@Param("t") Trace t, @Param("gridId") String gridId);

//    /**
//     * 运营公司操作员，根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
//     * 
//     * @param t
//     * @param page
//     * @return
//     */
//    List<YwUser> findPermedUserPage(@Param("t") Trace t, @Param("page") Page<YwUser> page);

    /**
     * 根据手机号码和dd同步状态获取用户对象
     *
     * @param t
     * @param mobile
     * @return
     */
    YwUser findByMobileAndDdStatus(@Param("t") Trace t, @Param("mobile") String mobile);

    /**
     * 根据UUID取用户对象
     *
     * @param t
     * @param uuid
     * @return
     */
    YwUser fetchByUuid(@Param("t") Trace t, @Param("uuid") String uuid);
}
