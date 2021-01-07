package com.linkallcloud.um.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.YwUser;

public interface IYwUserService extends IUserService<YwUser> {

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
     *
     * @param t
     * @param mobile
     * @return
     */
    YwUser findByMobileAndDdStatus(Trace t, String mobile);

    /**
     * 根据UUID获取用户对象
     *
     * @param t
     * @param uuid
     * @return
     */
    YwUser fetchByUuid(Trace t, String uuid);
}
