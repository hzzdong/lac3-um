package com.linkallcloud.um.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.YwUser;

public interface IYwUserActivity extends IUserActivity<YwUser> {

    /**
     * 运营公司操作员，根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
     *
     * @param t
     * @param page
     * @return
     */
    Page<YwUser> findPermedUserPage(Trace t, Page<YwUser> page);

    /**
     * 清除除userId外的所有手机号码为mobile的用户的手机号码为空
     *
     * @param t
     * @param mobile
     * @param userId
     */
    void cleanOtherUserMobileByUserId(Trace t, String mobile, Long userId);

    /**
     * 根据手机号码和dd同步状态获取用户对象
     * @param t
     * @param mobile
     * @return
     */
    YwUser findByMobileAndDdStatus(Trace t, String mobile);

}
