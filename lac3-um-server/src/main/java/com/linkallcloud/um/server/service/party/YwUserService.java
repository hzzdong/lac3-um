package com.linkallcloud.um.server.service.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwDepartmentActivity;
import com.linkallcloud.um.activity.party.IYwRoleActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.service.party.IYwUserService;

@Service
@Transactional(readOnly = true)
public class YwUserService extends
        UserService<YwUser, IYwUserActivity, YwDepartment, IYwDepartmentActivity, YwCompany, IYwCompanyActivity, YwRole, IYwRoleActivity>
        implements IYwUserService {

    @Autowired
    private IYwUserActivity ywUserActivity;

    @Autowired
    private IYwDepartmentActivity ywDepartmentActivity;

    @Autowired
    private IYwCompanyActivity ywCompanyActivity;

    @Autowired
    private IYwRoleActivity ywRoleActivity;

//	@Autowired
//	private IAreaActivity areaActivity;

    @Override
    public IYwUserActivity activity() {
        return ywUserActivity;
    }

    @Override
    protected IYwDepartmentActivity getDepartmentActivity() {
        return ywDepartmentActivity;
    }

    @Override
    protected IYwCompanyActivity getCompanyActivity() {
        return ywCompanyActivity;
    }

    @Override
    protected IYwRoleActivity getRoleActivity() {
        return ywRoleActivity;
    }

    // @CacheEvict(value = "YwUser", key = "\"ID-\" + #entity.id")
    @Transactional(readOnly = false)
    @Override
    public boolean update(Trace t, YwUser entity) throws BaseRuntimeException {
        return super.update(t, entity);
    }

    // @CacheEvict(value = "YwUser", key = "\"ID-\" + #id")
    @Transactional(readOnly = false)
    @Override
    public boolean delete(Trace t, Long id, String uuid) throws BaseRuntimeException {
        return super.delete(t, id, uuid);
    }

    // @Cacheable(value = "YwUser", key = "\"ID-\" + #id")
    @Transactional(readOnly = false)
    @Override
    public YwUser fetchById(Trace t, Long id) {
        return super.fetchById(t, id);
    }

    // @Cacheable(value = "YwUser", key = "\"ID-\" + #id")
    @Transactional(readOnly = false)
    @Override
    public YwUser fetchByIdUuid(Trace t, Long id, String uuid) {
        return super.fetchByIdUuid(t, id, uuid);
    }

    @Transactional(readOnly = false)
    @Override
    public void cleanOtherUserMobileByUserId(Trace t, String mobile, Long userId) {
        activity().cleanOtherUserMobileByUserId(t, mobile, userId);
    }

    @Override
    public YwUser findByMobileAndDdStatus(Trace t, String mobile) {
        return activity().findByMobileAndDdStatus(t, mobile);
    }

    @Override
    public YwUser fetchByUuid(Trace t, String uuid) {
        return activity().fetchByUuid(t, uuid);
    }

}
