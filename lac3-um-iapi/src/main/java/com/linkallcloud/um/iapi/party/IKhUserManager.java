package com.linkallcloud.um.iapi.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhUser;

public interface IKhUserManager extends IUserManager<KhUser> {

	/**
	 * 客户单位操作员，查公司或者部门下人员分页列表
	 * 
	 * @param t
	 * @param page
	 * @return
	 */
	Page<KhUser> findSelfUserPage(Trace t, Page<KhUser> page);

	/**
	 * 客户单位操作员，根据用户组织权限查某用户某应用全公司人员分页列表，Page中必须包含appId，userId，companyId参数
	 * 
	 * @param t
	 * @param page
	 * @return
	 */
	Page<KhUser> findPermedSelfUserPage(Trace t, Page<KhUser> page);

}
