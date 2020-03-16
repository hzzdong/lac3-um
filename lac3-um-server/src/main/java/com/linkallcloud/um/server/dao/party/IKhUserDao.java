package com.linkallcloud.um.server.dao.party;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhUserDao extends IUserDao<KhUser> {

    /**
     * 客户单位操作员，查公司或者部门下人员分页列表
     * 
     * @param t
     * @param page
     * @return
     */
    List<KhUser> findSelfUserPage(@Param("t") Trace t, @Param("page") Page<KhUser> page);

    /**
     * 客户单位操作员，根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
     * 
     * @param t
     * @param page
     * @return
     */
    List<KhUser> findPermedSelfUserPage(@Param("t") Trace t, @Param("page") Page<KhUser> page);

}
